package com.example.smartcareer.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class AssessmentRequest {
    
    @NotBlank(message = "测评类型不能为空")
    private String assessmentType;
    
    @NotNull(message = "答案不能为空")
    private List<AnswerItem> answers;
    
    private Integer totalScore;
    
    public String getAssessmentType() {
        return assessmentType;
    }
    
    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }
    
    public List<AnswerItem> getAnswers() {
        return answers;
    }
    
    public void setAnswers(List<AnswerItem> answers) {
        this.answers = answers;
    }
    
    public Integer getTotalScore() {
        return totalScore;
    }
    
    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }
    
    public static class AnswerItem {
        private Long questionId;
        private String answer;
        private Integer score;
        
        public Long getQuestionId() {
            return questionId;
        }
        
        public void setQuestionId(Long questionId) {
            this.questionId = questionId;
        }
        
        public String getAnswer() {
            return answer;
        }
        
        public void setAnswer(String answer) {
            this.answer = answer;
        }
        
        public Integer getScore() {
            return score;
        }
        
        public void setScore(Integer score) {
            this.score = score;
        }
    }
}
