// DynamicSchedulerService.java
package com.tool.AutomatedRiskAssessment.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.*;

@Service
public class DynamicSchedulerService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    public void scheduleTask(String userName, LocalDateTime runAt, Runnable task) {
        long delay = Duration.between(LocalDateTime.now(), runAt).toMillis();

        if (delay > 0) {
            scheduler.schedule(() -> {
                System.out.println("Running task for: " + userName);
                task.run();
            }, delay, TimeUnit.MILLISECONDS);
        } else {
            System.out.println("Invalid time. It’s in the past!");
        }
    }
}
