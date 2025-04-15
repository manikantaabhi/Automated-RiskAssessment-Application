package com.tool.AutomatedRiskAssessment.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tool.AutomatedRiskAssessment.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByEmailOrderByTimestampDesc(Optional<String> email);
    Long countByEmailAndReadFalse(Optional<String> email);
}

