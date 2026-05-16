package com.example.smartcareer.service.impl;

import com.example.smartcareer.dto.request.AssessmentRequest;
import com.example.smartcareer.dto.response.AssessmentResponse;
import com.example.smartcareer.entity.Assessment;
import com.example.smartcareer.entity.AssessmentQuestion;
import com.example.smartcareer.entity.JobSeeker;
import com.example.smartcareer.exception.BusinessException;
import com.example.smartcareer.exception.ErrorCode;
import com.example.smartcareer.repository.AssessmentQuestionRepository;
import com.example.smartcareer.repository.AssessmentRepository;
import com.example.smartcareer.repository.JobSeekerRepository;
import com.example.smartcareer.service.AssessmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssessmentServiceImpl implements AssessmentService {
    
    @Autowired
    private AssessmentRepository assessmentRepository;
    
    @Autowired
    private AssessmentQuestionRepository questionRepository;
    
    @Autowired
    private JobSeekerRepository jobSeekerRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    public List<String> getAssessmentCategories() {
        return questionRepository.findAllCategories();
    }
    
    @Override
    public List<AssessmentResponse.QuestionResponse> getQuestionsByCategory(String category) {
        List<AssessmentQuestion> questions = questionRepository.findActiveByCategory(category);
        return questions.stream().map(this::convertToQuestionResponse).collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public AssessmentResponse submitAssessment(Long userId, AssessmentRequest request) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        List<AssessmentQuestion> questions = questionRepository.findActiveByCategory(request.getAssessmentType());
        
        int totalScore = 0;
        Map<String, Integer> skillScores = new HashMap<>();
        List<Map<String, Object>> answerList = new ArrayList<>();
        
        // 如果前端传来了分数，优先使用前端的分数
        if (request.getTotalScore() != null && request.getTotalScore() > 0) {
            totalScore = request.getTotalScore();
        }
        
        for (AssessmentRequest.AnswerItem answer : request.getAnswers()) {
            AssessmentQuestion question = null;
            
            // 尝试通过ID查找题目
            if (answer.getQuestionId() != null && !questions.isEmpty()) {
                question = questions.stream()
                        .filter(q -> q.getId().equals(answer.getQuestionId()))
                        .findFirst()
                        .orElse(null);
            }
            
            int answerScore = answer.getScore() != null ? answer.getScore() : 80;
            
            if (question != null) {
                int score = calculateScore(question, answer.getAnswer());
                answerScore = score;
                
                String skillTag = question.getSkillTag();
                if (skillTag != null && !skillTag.isEmpty()) {
                    skillScores.merge(skillTag, score, Integer::sum);
                }
            }
            
            // 如果前端没有传来总分数，则累加每题分数
            if (request.getTotalScore() == null || request.getTotalScore() <= 0) {
                totalScore += answerScore;
            }
            
            Map<String, Object> answerMap = new HashMap<>();
            answerMap.put("questionId", answer.getQuestionId());
            answerMap.put("answer", answer.getAnswer());
            answerMap.put("score", answerScore);
            answerList.add(answerMap);
        }
        
        // 如果前端没有传来总分数，计算平均分
        if (request.getTotalScore() == null || request.getTotalScore() <= 0) {
            if (!answerList.isEmpty()) {
                totalScore = totalScore / answerList.size();
            }
        }
        
        String skillTags = generateSkillTags(skillScores);
        String careerAdvice = generateCareerAdvice(request.getAssessmentType(), totalScore, skillScores);
        String result = generateResult(request.getAssessmentType(), totalScore);
        
        Assessment assessment = new Assessment();
        assessment.setJobSeeker(jobSeeker);
        assessment.setAssessmentType(request.getAssessmentType());
        
        try {
            assessment.setQuestions(objectMapper.writeValueAsString(questions));
            assessment.setAnswers(objectMapper.writeValueAsString(answerList));
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
        }
        
        assessment.setResult(result);
        assessment.setSkillTags(skillTags);
        assessment.setCareerAdvice(careerAdvice);
        assessment.setScore(totalScore);
        
        Assessment saved = assessmentRepository.save(assessment);
        
        return convertToResponse(saved);
    }
    
    @Override
    public AssessmentResponse getAssessmentById(Long assessmentId) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
        return convertToResponse(assessment);
    }
    
    @Override
    public Page<AssessmentResponse> getMyAssessments(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Assessment> assessments = assessmentRepository.findByJobSeekerIdOrderByTime(userId, pageable);
        return assessments.map(this::convertToResponse);
    }
    
    @Override
    public List<AssessmentResponse> getAssessmentsByType(Long userId, String type) {
        List<Assessment> assessments = assessmentRepository.findByJobSeekerAndType(userId, type);
        return assessments.stream().map(this::convertToResponse).collect(Collectors.toList());
    }
    
    @Override
    public Map<String, Object> getAssessmentStatistics(Long userId) {
        List<Assessment> assessments = assessmentRepository.findByJobSeekerIdOrderByTime(userId);
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalCount", assessments.size());
        
        Map<String, Long> typeCount = assessments.stream()
                .collect(Collectors.groupingBy(Assessment::getAssessmentType, Collectors.counting()));
        statistics.put("typeDistribution", typeCount);
        
        if (!assessments.isEmpty()) {
            Double avgScore = assessments.stream()
                    .mapToInt(Assessment::getScore)
                    .average()
                    .orElse(0.0);
            statistics.put("averageScore", avgScore);
            
            Assessment latest = assessments.get(0);
            statistics.put("latestScore", latest.getScore());
            statistics.put("latestType", latest.getAssessmentType());
        }
        
        return statistics;
    }
    
    @Override
    @Transactional
    public void deleteAssessment(Long userId, Long assessmentId) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
        
        if (!assessment.getJobSeeker().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        
        assessmentRepository.delete(assessment);
    }
    
    private int calculateScore(AssessmentQuestion question, String answer) {
        if (question.getOptions() == null || question.getOptions().isEmpty()) {
            return question.getScore() != null ? question.getScore() : 0;
        }
        
        try {
            List<Map<String, Object>> options = objectMapper.readValue(
                    question.getOptions(),
                    new TypeReference<List<Map<String, Object>>>() {}
            );
            
            for (Map<String, Object> option : options) {
                String label = (String) option.get("label");
                if (label != null && label.equals(answer)) {
                    Object scoreObj = option.get("score");
                    if (scoreObj instanceof Number) {
                        return ((Number) scoreObj).intValue();
                    }
                }
            }
        } catch (JsonProcessingException e) {
            return 0;
        }
        
        return 0;
    }
    
    private String generateSkillTags(Map<String, Integer> skillScores) {
        return skillScores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(","));
    }
    
    private String generateCareerAdvice(String type, int totalScore, Map<String, Integer> skillScores) {
        StringBuilder advice = new StringBuilder();
        
        if ("PERSONALITY".equals(type)) {
            if (totalScore >= 80) {
                advice.append("您具有较强的领导力和沟通能力，适合担任管理或团队领导岗位。");
            } else if (totalScore >= 60) {
                advice.append("您具有较好的团队协作能力，适合需要协调沟通的工作岗位。");
            } else {
                advice.append("建议您加强沟通技巧和团队协作能力的培养。");
            }
        } else if ("SKILL".equals(type)) {
            advice.append("根据您的技能测评结果，");
            if (!skillScores.isEmpty()) {
                String topSkill = skillScores.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse("");
                advice.append("您的").append(topSkill).append("能力较强，建议继续深化。");
            }
        } else if ("CAREER".equals(type)) {
            if (totalScore >= 70) {
                advice.append("您的职业规划较为清晰，建议继续关注行业发展动态，提升核心竞争力。");
            } else {
                advice.append("建议您更深入地了解自己的职业兴趣和发展方向，制定更明确的职业规划。");
            }
        }
        
        return advice.toString();
    }
    
    private String generateResult(String type, int totalScore) {
        String level;
        if (totalScore >= 90) {
            level = "优秀";
        } else if (totalScore >= 70) {
            level = "良好";
        } else if (totalScore >= 60) {
            level = "合格";
        } else {
            level = "需提升";
        }
        
        String typeName;
        switch (type) {
            case "PERSONALITY":
                typeName = "性格测评";
                break;
            case "SKILL":
                typeName = "技能测评";
                break;
            case "CAREER":
                typeName = "职业规划测评";
                break;
            default:
                typeName = "综合测评";
        }
        
        return typeName + "结果：" + level + "（得分：" + totalScore + "分）";
    }
    
    private AssessmentResponse.QuestionResponse convertToQuestionResponse(AssessmentQuestion question) {
        AssessmentResponse.QuestionResponse response = new AssessmentResponse.QuestionResponse();
        response.setId(question.getId());
        response.setCategory(question.getCategory());
        response.setQuestionText(question.getQuestionText());
        response.setQuestionType(question.getQuestionType());
        
        if (question.getOptions() != null && !question.getOptions().isEmpty()) {
            try {
                List<Map<String, Object>> options = objectMapper.readValue(
                        question.getOptions(),
                        new TypeReference<List<Map<String, Object>>>() {}
                );
                
                List<AssessmentResponse.OptionResponse> optionResponses = options.stream()
                        .map(opt -> {
                            AssessmentResponse.OptionResponse optionResponse = new AssessmentResponse.OptionResponse();
                            optionResponse.setLabel((String) opt.get("label"));
                            optionResponse.setText((String) opt.get("text"));
                            if (opt.get("score") instanceof Number) {
                                optionResponse.setScore(((Number) opt.get("score")).intValue());
                            }
                            return optionResponse;
                        })
                        .collect(Collectors.toList());
                
                response.setOptions(optionResponses);
            } catch (JsonProcessingException e) {
                response.setOptions(new ArrayList<>());
            }
        }
        
        return response;
    }
    
    private AssessmentResponse convertToResponse(Assessment assessment) {
        AssessmentResponse response = new AssessmentResponse();
        response.setId(assessment.getId());
        response.setAssessmentType(assessment.getAssessmentType());
        response.setResult(assessment.getResult());
        response.setSkillTags(assessment.getSkillTags());
        response.setCareerAdvice(assessment.getCareerAdvice());
        response.setScore(assessment.getScore());
        response.setCreateTime(assessment.getCreateTime());
        
        if (assessment.getQuestions() != null) {
            try {
                List<AssessmentQuestion> questions = objectMapper.readValue(
                        assessment.getQuestions(),
                        new TypeReference<List<AssessmentQuestion>>() {}
                );
                response.setQuestions(questions.stream()
                        .map(this::convertToQuestionResponse)
                        .collect(Collectors.toList()));
            } catch (JsonProcessingException e) {
                response.setQuestions(new ArrayList<>());
            }
        }
        
        return response;
    }
}
