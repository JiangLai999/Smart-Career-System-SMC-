package com.example.smartcareer.service.impl;

import com.example.smartcareer.dto.request.JobFilterRequest;
import com.example.smartcareer.dto.request.JobPublishRequest;
import com.example.smartcareer.dto.response.JobResponse;
import com.example.smartcareer.entity.Enterprise;
import com.example.smartcareer.entity.EnterpriseHR;
import com.example.smartcareer.entity.Job;
import com.example.smartcareer.exception.BusinessException;
import com.example.smartcareer.exception.ErrorCode;
import com.example.smartcareer.repository.EnterpriseHRRepository;
import com.example.smartcareer.repository.EnterpriseRepository;
import com.example.smartcareer.repository.JobRepository;
import com.example.smartcareer.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private EnterpriseRepository enterpriseRepository;
    
    @Autowired
    private EnterpriseHRRepository enterpriseHRRepository;
    
    @Override
    @Transactional
    public Job publishJob(JobPublishRequest request, Long enterpriseId, Long hrId) {
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTERPRISE_NOT_FOUND));
        
        EnterpriseHR hr = null;
        if (hrId != null) {
            hr = enterpriseHRRepository.findById(hrId).orElse(null);
        }
        
        Job job = new Job();
        job.setEnterprise(enterprise);
        job.setHr(hr);
        job.setTitle(request.getTitle());
        job.setCategory(request.getCategory());
        job.setLocation(request.getLocation());
        job.setEducation(request.getEducation());
        job.setExperience(request.getExperience());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setRecruitNumber(request.getRecruitNumber());
        job.setDescription(request.getDescription());
        job.setRequirement(request.getRequirement());
        job.setResponsibility(request.getResponsibility());
        job.setWelfare(request.getWelfare());
        job.setWorkType(request.getWorkType());
        job.setStatus("PUBLISHED");
        
        return jobRepository.save(job);
    }
    
    @Override
    @Transactional
    public Job updateJob(Long id, Job job) {
        Job existingJob = getJobById(id);
        existingJob.setTitle(job.getTitle());
        existingJob.setCategory(job.getCategory());
        existingJob.setLocation(job.getLocation());
        existingJob.setEducation(job.getEducation());
        existingJob.setExperience(job.getExperience());
        existingJob.setSalaryMin(job.getSalaryMin());
        existingJob.setSalaryMax(job.getSalaryMax());
        existingJob.setRecruitNumber(job.getRecruitNumber());
        existingJob.setDescription(job.getDescription());
        existingJob.setRequirement(job.getRequirement());
        existingJob.setResponsibility(job.getResponsibility());
        existingJob.setWelfare(job.getWelfare());
        existingJob.setWorkType(job.getWorkType());
        return jobRepository.save(existingJob);
    }
    
    @Override
    @Transactional
    public void deleteJob(Long id) {
        Job job = getJobById(id);
        job.setStatus("DELETED");
        jobRepository.save(job);
    }
    
    @Override
    public Job getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.JOB_NOT_FOUND));
        
        // 用户端只能查看发布状态的职位
        if (!"PUBLISHED".equals(job.getStatus()) && !"ACTIVE".equals(job.getStatus())) {
            throw new BusinessException(ErrorCode.JOB_NOT_FOUND, "职位不存在或已下线");
        }
        
        return job;
    }
    
    @Override
    public Page<Job> getJobs(Pageable pageable) {
        return jobRepository.findByStatusIn(Arrays.asList("PUBLISHED", "ACTIVE"), pageable);
    }
    
    @Override
    public Page<Job> searchJobs(String keyword, Pageable pageable) {
        return jobRepository.searchByKeywordAndStatusIn(keyword, Arrays.asList("PUBLISHED", "ACTIVE"), pageable);
    }
    
    @Override
    public Page<Job> filterJobs(JobFilterRequest request, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Job> query = cb.createQuery(Job.class);
        Root<Job> root = query.from(Job.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        predicates.add(root.get("status").in(Arrays.asList("PUBLISHED", "ACTIVE")));
        
        if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
            Predicate titlePredicate = cb.like(root.get("title"), "%" + request.getKeyword() + "%");
            Predicate descPredicate = cb.like(root.get("description"), "%" + request.getKeyword() + "%");
            predicates.add(cb.or(titlePredicate, descPredicate));
        }
        
        if (request.getCategory() != null && !request.getCategory().trim().isEmpty()) {
            predicates.add(cb.equal(root.get("category"), request.getCategory()));
        }
        
        if (request.getLocation() != null && !request.getLocation().trim().isEmpty()) {
            predicates.add(cb.like(root.get("location"), "%" + request.getLocation() + "%"));
        }
        
        if (request.getEducation() != null && !request.getEducation().trim().isEmpty()) {
            predicates.add(cb.equal(root.get("education"), request.getEducation()));
        }
        
        if (request.getExperience() != null && !request.getExperience().trim().isEmpty()) {
            predicates.add(cb.equal(root.get("experience"), request.getExperience()));
        }
        
        if (request.getJobType() != null && !request.getJobType().trim().isEmpty()) {
            predicates.add(cb.equal(root.get("workType"), request.getJobType()));
        }
        
        if (request.getSalaryMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("salaryMax"), request.getSalaryMin()));
        }
        
        if (request.getSalaryMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("salaryMin"), request.getSalaryMax()));
        }
        
        query.where(predicates.toArray(new Predicate[0]));
        
        if (request.getSortBy() != null && !request.getSortBy().trim().isEmpty()) {
            if ("asc".equalsIgnoreCase(request.getSortOrder())) {
                query.orderBy(cb.asc(root.get(request.getSortBy())));
            } else {
                query.orderBy(cb.desc(root.get(request.getSortBy())));
            }
        } else {
            query.orderBy(cb.desc(root.get("publishTime")));
        }
        
        List<Job> jobs = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Job> countRoot = countQuery.from(Job.class);
        countQuery.select(cb.count(countRoot));
        countQuery.where(predicates.toArray(new Predicate[0]));
        Long total = entityManager.createQuery(countQuery).getSingleResult();
        
        return new PageImpl<>(jobs, pageable, total);
    }
    
    @Override
    public List<Job> getJobsByEnterprise(Long enterpriseId) {
        return jobRepository.findByEnterpriseId(enterpriseId);
    }
    
    @Override
    public List<Job> getJobsByHr(Long hrId) {
        return jobRepository.findByHrId(hrId);
    }
    
    @Override
    public Page<Job> getJobsByCategory(String category, Pageable pageable) {
        Page<Job> jobs = jobRepository.findByStatus("PUBLISHED", pageable);
        List<Job> filtered = jobs.getContent().stream()
                .filter(job -> category.equals(job.getCategory()))
                .collect(Collectors.toList());
        return new PageImpl<>(filtered, pageable, jobs.getTotalElements());
    }
    
    @Override
    public Page<Job> getJobsByLocation(String location, Pageable pageable) {
        Page<Job> jobs = jobRepository.findByStatus("PUBLISHED", pageable);
        List<Job> filtered = jobs.getContent().stream()
                .filter(job -> job.getLocation() != null && job.getLocation().contains(location))
                .collect(Collectors.toList());
        return new PageImpl<>(filtered, pageable, jobs.getTotalElements());
    }
    
    @Override
    @Transactional
    public void incrementViewCount(Long jobId) {
        jobRepository.incrementViewCount(jobId);
    }
    
    @Override
    @Transactional
    public void incrementApplyCount(Long jobId) {
        jobRepository.incrementApplyCount(jobId);
    }
    
    @Override
    public JobResponse convertToResponse(Job job) {
        JobResponse response = new JobResponse();
        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setCategory(job.getCategory());
        response.setLocation(job.getLocation());
        response.setEducation(job.getEducation());
        response.setExperience(job.getExperience());
        response.setSalaryMin(job.getSalaryMin());
        response.setSalaryMax(job.getSalaryMax());
        response.setRecruitNumber(job.getRecruitNumber());
        response.setDescription(job.getDescription());
        response.setRequirement(job.getRequirement());
        response.setResponsibility(job.getResponsibility());
        response.setWelfare(job.getWelfare());
        response.setWorkType(job.getWorkType());
        response.setStatus(job.getStatus());
        response.setPublishTime(job.getPublishTime());
        response.setViewCount(job.getViewCount());
        response.setApplyCount(job.getApplyCount());
        
        if (job.getEnterprise() != null) {
            response.setEnterpriseId(job.getEnterprise().getId());
            response.setEnterpriseName(job.getEnterprise().getEnterpriseName());
            response.setEnterpriseLogo(job.getEnterprise().getLogo());
        }
        
        return response;
    }
}
