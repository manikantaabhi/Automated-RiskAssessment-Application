package com.tool.AutomatedRiskAssessment.controller;

import com.tool.AutomatedRiskAssessment.model.Notification;
import com.tool.AutomatedRiskAssessment.repo.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    // GET endpoint returns only unread notifications for the provided userId
    @GetMapping
    public List<Notification> getUnreadNotifications(@RequestParam Long userId) {
        return notificationRepository.findByUserIdAndReadFlagFalse(userId);
    }

    // PATCH endpoint to mark a notification as read
    @PatchMapping("/{id}")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id, @RequestBody Notification update) {
        Optional<Notification> optNotification = notificationRepository.findById(id);
        if (!optNotification.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Notification notification = optNotification.get();
        // Update the read flag based on the incoming JSON (make sure JSON property "read" is mapped correctly)
        notification.setReadFlag(update.isReadFlag());
        notificationRepository.save(notification);
        return ResponseEntity.ok(notification);
    }
}