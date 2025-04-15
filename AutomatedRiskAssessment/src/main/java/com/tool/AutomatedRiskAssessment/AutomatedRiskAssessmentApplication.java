package com.tool.AutomatedRiskAssessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
@EnableAsync 
public class AutomatedRiskAssessmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutomatedRiskAssessmentApplication.class, args);
	}

}
