package com.tool.AutomatedRiskAssessment.repo;

import com.tool.AutomatedRiskAssessment.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
    List<Notification> findByUserIdAndReadFlagFalse(Long userId);
}