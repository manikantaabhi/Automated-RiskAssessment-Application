// SchedulingController.java
package com.tool.AutomatedRiskAssessment.controller;

import com.tool.AutomatedRiskAssessment.dto.ScheduleRequest;
import com.tool.AutomatedRiskAssessment.service.DynamicSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/scheduler")
public class SchedulingController {

    @Autowired
    private DynamicSchedulerService schedulerService;

    @Autowired
    private NvdVulnerabilityScheduler vulnerabilityScheduler;

    @PostMapping("/schedule")
    public ResponseEntity<Map<String, String>> scheduleJob(@RequestBody ScheduleRequest request) {
        LocalDateTime scheduledTime = request.getScheduledTime();
        String userName = request.getUserName();

        schedulerService.scheduleTask(userName, scheduledTime, () -> 
            vulnerabilityScheduler.checkVulnerabilitiesForUser(userName)
        );

        Map<String, String> response = new HashMap<>();
        response.put("message", "Scheduled vulnerability check for " + userName + " at " + scheduledTime);
        return ResponseEntity.ok(response);
    }

}
