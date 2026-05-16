package com.example.smartcareer.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class AssessmentResponse {
    
    private Long id;
    
    private String assessmentType;
    
    private List<QuestionResponse> questions;
    
    private String result;
    
    private String skillTags;
    
    private String careerAdvice;
    
    private Integer score;
    
    private LocalDateTime createTime;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getAssessmentType() {
        return assessmentType;
    }
    
    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }
    
    public List<QuestionResponse> getQuestions() {
        return questions;
    }
    
    public void setQuestions(List<QuestionResponse> questions) {
        this.questions = questions;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public String getSkillTags() {
        return skillTags;
    }
    
    public void setSkillTags(String skillTags) {
        this.skillTags = skillTags;
    }
    
    public String getCareerAdvice() {
        return careerAdvice;
    }
    
    public void setCareerAdvice(String careerAdvice) {
        this.careerAdvice = careerAdvice;
    }
    
    public Integer getScore() {
        return score;
    }
    
    public void setScore(Integer score) {
        this.score = score;
    }
    
    public Integer getTotalScore() {
        return score;
    }
    
    public void setTotalScore(Integer totalScore) {
        this.score = totalScore;
    }
    
    public String getType() {
        return assessmentType;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public static class QuestionResponse {
        private Long id;
        private String category;
        private String questionText;
        private List<OptionResponse> options;
        private String questionType;
        
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public String getCategory() {
            return category;
        }
        
        public void setCategory(String category) {
            this.category = category;
        }
        
        public String getQuestionText() {
            return questionText;
        }
        
        public void setQuestionText(String questionText) {
            this.questionText = questionText;
        }
        
        public List<OptionResponse> getOptions() {
            return options;
        }
        
        public void setOptions(List<OptionResponse> options) {
            this.options = options;
        }
        
        public String getQuestionType() {
            return questionType;
        }
        
        public void setQuestionType(String questionType) {
            this.questionType = questionType;
        }
    }
    
    public static class OptionResponse {
        private String label;
        private String text;
        private Integer score;
        
        public String getLabel() {
            return label;
        }
        
        public void setLabel(String label) {
            this.label = label;
        }
        
        public String getText() {
            return text;
        }
        
        public void setText(String text) {
            this.text = text;
        }
        
        public Integer getScore() {
            return score;
        }
        
        public void setScore(Integer score) {
            this.score = score;
        }
    }
}
