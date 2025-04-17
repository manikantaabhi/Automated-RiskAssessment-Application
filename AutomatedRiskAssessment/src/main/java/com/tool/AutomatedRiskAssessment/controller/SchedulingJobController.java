package com.tool.AutomatedRiskAssessment.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tool.AutomatedRiskAssessment.model.ScheduledJob;
import com.tool.AutomatedRiskAssessment.model.UserVulnerability;
import com.tool.AutomatedRiskAssessment.model.Vulnerability;
import com.tool.AutomatedRiskAssessment.repo.ScheduledJobRepository;
import com.tool.AutomatedRiskAssessment.repo.UserVulnerabilityRepository;
import com.tool.AutomatedRiskAssessment.repo.VulnerabilityRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/jobs")
public class SchedulingJobController {

    @Autowired
    private ScheduledJobRepository jobRepository;
    

    @Autowired
    private UserVulnerabilityRepository userVulnerabilityRepository;
    
    @Autowired
    private VulnerabilityRepository repository;


    @PostMapping("/schedule")
    public ResponseEntity<?> schedule(@RequestBody ScheduledJob job) {
        System.out.println("scheduling..." + job.getUserName());

        int count = jobRepository.countByScheduledTime(job.getScheduledTime());

        List<ScheduledJob> userJobs = jobRepository.findByUserName(job.getUserName());

        if (count <= 0) {
            job.setExecuted(false);
            jobRepository.save(job);
            return ResponseEntity.ok(userJobs);
        }

        // Conflict response with existing jobs
        return ResponseEntity
                .status(409) // HTTP 409 Conflict
                .body("Job already scheduled for " + job.getScheduledTime() + ". User's current jobs: " + userJobs.size());
    }
    
    @GetMapping("/jobs")
    public ResponseEntity<?> getJobs(@RequestParam String userName) {

        List<ScheduledJob> userJobs = jobRepository.findByUserName(userName);
        return ResponseEntity.ok(userJobs);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        Optional<ScheduledJob> job = jobRepository.findById(id);
        
        if (job.isPresent()) {
            jobRepository.delete(job.get());
            return ResponseEntity.ok("Job deleted successfully.");
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found.");
    }


    
    @GetMapping("all")
    public List<Vulnerability> getVulnerabilitiesForUser(@RequestParam String userName) {
    	
    	List<String> cveIds=userVulnerabilityRepository.findCveIdsByUsername(userName);
    	
    	List<Vulnerability> vulnerabilities = new ArrayList<Vulnerability>();
    	for(String cveId:cveIds) {
    		Optional<Vulnerability> vul= repository.findByCveId(cveId);
    		vul.ifPresent(vulnerabilities::add);
    	}
        return vulnerabilities;
    }
    @GetMapping("/count")
    public int getVulnerabilitiesForUserCount(@RequestParam String userName) {
    	List<String> cveIds=userVulnerabilityRepository.findCveIdsByUsername(userName);
    	return cveIds.size();
    }
    
    
}
