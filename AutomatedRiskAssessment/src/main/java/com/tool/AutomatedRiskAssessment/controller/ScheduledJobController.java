package com.tool.AutomatedRiskAssessment.controller;

import com.tool.AutomatedRiskAssessment.model.ScheduledJob;
import com.tool.AutomatedRiskAssessment.repo.ScheduledJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scheduled-jobs")
public class ScheduledJobController {

    @Autowired
    private ScheduledJobRepository jobRepository;

    @PostMapping
    public ScheduledJob createJob(@RequestBody ScheduledJob job) {
        // Save the job and return the saved entity
        return jobRepository.save(job);
    }

    @GetMapping
    public ResponseEntity<?> getJobsByUser(@RequestParam Long userId) {
        // Only return jobs for the given userId that are still scheduled
        return ResponseEntity.ok(jobRepository.findByUserIdAndStatus(userId, ScheduledJob.JobStatus.SCHEDULED));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelJob(@PathVariable Long id) {
        return jobRepository.findById(id)
                .map(job -> {
                    job.setStatus(ScheduledJob.JobStatus.CANCELLED);
                    jobRepository.save(job);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}