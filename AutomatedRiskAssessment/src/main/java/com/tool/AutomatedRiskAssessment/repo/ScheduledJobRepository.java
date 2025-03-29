package com.tool.AutomatedRiskAssessment.repo;

import com.tool.AutomatedRiskAssessment.model.ScheduledJob;
import com.tool.AutomatedRiskAssessment.model.ScheduledJob.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduledJobRepository extends JpaRepository<ScheduledJob, Long> {

    @Query("SELECT j FROM ScheduledJob j WHERE j.userId = :userId AND j.status = :status")
    List<ScheduledJob> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") JobStatus status);
}