package com.tool.AutomatedRiskAssessment.controller;
/* Checks jobs repo for every minute and if it fnds out any job it picksup and schedule the job*/
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tool.AutomatedRiskAssessment.model.ScheduledJob;
import com.tool.AutomatedRiskAssessment.repo.ScheduledJobRepository;
import com.tool.AutomatedRiskAssessment.service.DynamicSchedulerService;

@Component
public class DatabaseBackedScheduler {

    @Autowired
    private ScheduledJobRepository jobRepository;

    @Autowired
    private DynamicSchedulerService schedulerService;

    @Autowired
    private NvdVulnerabilityScheduler nvdScheduler;

    @Scheduled(fixedRate = 60000) // Check every 1 minute
    public void pollDatabaseAndScheduleTasks() {
        LocalDateTime now = LocalDateTime.now();
        List<ScheduledJob> pendingJobs = jobRepository.findByExecutedFalseAndScheduledTimeBefore(now);

        for (ScheduledJob job : pendingJobs) {
            System.out.println("Scheduling job for user: " + job.getUserName());

            schedulerService.scheduleTask(job.getUserName(), job.getScheduledTime(), () -> {
                nvdScheduler.checkVulnerabilitiesForUser(job.getUserName());
                System.out.println("Executed vulnerability check for: " + job.getUserName());
            });

            // Mark job as executed
            job.setExecuted(true);
            jobRepository.save(job);
        }
    }
}
