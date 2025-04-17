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
        LocalDateTime now = LocalDateTime.now();
        long delay = Duration.between(now, runAt).toMillis();

        // If the scheduled time is too close to the current time, adjust it
        if (delay < 0) {
            runAt = runAt.plusMinutes(1).withSecond(0).withNano(0); // Round to the next full minute
            delay = Duration.between(now, runAt).toMillis(); // Recalculate delay
            System.out.println("Adjusted runAt to the next minute: " + runAt);
        }
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
