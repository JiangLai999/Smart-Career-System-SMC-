package com.example.smartcareer.repository;

import com.example.smartcareer.entity.AssessmentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentQuestionRepository extends JpaRepository<AssessmentQuestion, Long> {
    
    List<AssessmentQuestion> findByCategory(String category);
    
    List<AssessmentQuestion> findByStatus(Integer status);
    
    @Query("SELECT q FROM AssessmentQuestion q WHERE q.category = :category AND q.status = 1 ORDER BY q.sortOrder")
    List<AssessmentQuestion> findActiveByCategory(@Param("category") String category);
    
    @Query("SELECT DISTINCT q.category FROM AssessmentQuestion q WHERE q.status = 1")
    List<String> findAllCategories();
    
    @Query("SELECT COUNT(q) FROM AssessmentQuestion q WHERE q.status = 1")
    Long countActiveQuestions();
}
