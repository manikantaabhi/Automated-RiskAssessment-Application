// ScheduleRequest.java
package com.tool.AutomatedRiskAssessment.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ScheduleRequest {
    private String userName;
    private LocalDateTime scheduledTime;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public LocalDateTime getScheduledTime() {
		return scheduledTime;
	}
	public void setScheduledTime(LocalDateTime scheduledTime) {
		this.scheduledTime = scheduledTime;
	}
    
    
}
