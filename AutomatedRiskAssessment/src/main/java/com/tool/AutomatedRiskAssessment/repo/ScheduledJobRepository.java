package com.tool.AutomatedRiskAssessment.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tool.AutomatedRiskAssessment.model.ScheduledJob;

public interface ScheduledJobRepository extends JpaRepository<ScheduledJob, Long> {
    List<ScheduledJob> findByExecutedFalseAndScheduledTimeBefore(LocalDateTime now);

    int countByScheduledTime(LocalDateTime scheduledTime); // simpler, cleaner, correct

	List<ScheduledJob> findByUserName(String userName);

}
