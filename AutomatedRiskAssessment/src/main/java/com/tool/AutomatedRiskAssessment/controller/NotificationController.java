package com.tool.AutomatedRiskAssessment.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tool.AutomatedRiskAssessment.dto.VulnerabilityDataResponse;
import com.tool.AutomatedRiskAssessment.model.Notification;
import com.tool.AutomatedRiskAssessment.model.Vulnerability;
import com.tool.AutomatedRiskAssessment.repo.NotificationRepository;
import com.tool.AutomatedRiskAssessment.repo.UserRepository;
import com.tool.AutomatedRiskAssessment.repo.VulnerabilityRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private VulnerabilityRepository vulnerabilityRepository;
	
	@Autowired
	private UserRepository repository;
	
	
	@GetMapping("/count")
	public Long getUnreadCount(@RequestParam String userName) {
		Optional<String> email=repository.findEmailByUsername(userName);
	    List<Notification> allNotifications = notificationRepository.findByEmailOrderByTimestampDesc(email);

	    Set<String> uniqueCveIds = new HashSet<>();

	    for (Notification notification : allNotifications) {
	        if (!notification.isRead()) {
	            String[] cveList = notification.getMessage().split(",\\s*");
	            for (String cve : cveList) {
	                uniqueCveIds.add(cve.trim().toUpperCase());
	            }
	        }
	    }

	    return (long) uniqueCveIds.size();
	}


	
	@GetMapping("/all")
	public List<Vulnerability> getNotifications(@RequestParam String userName) {
		Optional<String> email=repository.findEmailByUsername(userName);
	    List<Notification> notifications = notificationRepository.findByEmailOrderByTimestampDesc(email);

	    // Mark all as read
//	    for (Notification n : notifications) {
//	        n.setRead(true);
//	    }
	    System.out.println(notifications);
	    List<Vulnerability> data = new ArrayList<Vulnerability>();
	    notificationRepository.saveAll(notifications);
	    
	    for(Notification not:notifications) {
	    	System.out.println(not.getMessage());
	    	
	    	String[] list = not.getMessage().split(", ");
	    	for(String cve:list) {
	    		Optional<Vulnerability> str = vulnerabilityRepository.findByCveId(cve);
	    		str.ifPresent(data::add);
	    	}
	    }
	    return data;
	}
	
	@DeleteMapping("/delete-cve")
	public String deleteCve(@RequestParam String userName, @RequestBody List<String> cveId) {
		System.out.println("here");
		Optional<String> email=repository.findEmailByUsername(userName);
	    List<Notification> notifications = notificationRepository.findByEmailOrderByTimestampDesc(email);
	    for(String fromCve:cveId)
	    {
	    String targetCve = fromCve.trim().toUpperCase(); // Normalize input
	    boolean updated = false;

	    for (Notification notification : notifications) {
	        String[] cveArray = notification.getMessage().split(",\\s*");
	        List<String> updatedCves = new ArrayList<>();

	        for (String cve : cveArray) {
	            if (!cve.trim().equalsIgnoreCase(targetCve)) {
	                updatedCves.add(cve.trim());
	            }
	        }

	        // If CVE was removed, update message
	        if (updatedCves.size() < cveArray.length) {
	            notification.setMessage(String.join(", ", updatedCves));
	            updated = true;
	        }
	    }

	    if (updated) {
	        notificationRepository.saveAll(notifications);
	    } 
	    }
		return " removed successfully.";
	}


}
