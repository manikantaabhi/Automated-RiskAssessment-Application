package com.tool.AutomatedRiskAssessment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tool.AutomatedRiskAssessment.dto.VulnerabilityDataResponse;
import com.tool.AutomatedRiskAssessment.model.Notification;
import com.tool.AutomatedRiskAssessment.repo.*;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NvdFetcherService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private CheckVulnerabilityService checkVulnerabilityService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Autowired
    private JavaMailSender mailSender;
    
    @Scheduled(fixedRate = 3 * 60 * 1000) // Every 5 minutes
    public void fetchNvdData() {
        // Round down current time to the nearest 5-minute mark
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        int minute = now.getMinute();
        int roundedMinute = (minute / 3) * 3;
        LocalDateTime start = now.withMinute(roundedMinute);
        LocalDateTime end = start.plusMinutes(3);

        String pubStartDate = formatter.format(start);
        String pubEndDate = formatter.format(end);
        System.out.println(pubStartDate+" "+pubEndDate);
        String url = String.format("https://services.nvd.nist.gov/rest/json/cves/2.0?pubStartDate=%s&pubEndDate=%s", pubStartDate, pubEndDate);

        try {
        	Thread.sleep(500);
            Map<String, Object> response = (Map<String, Object>) restTemplate.getForObject(url, Map.class);
            if (response.containsKey("vulnerabilities")) {
                System.out.println(response);
                List<VulnerabilityDataResponse> str= checkVulnerabilityService.parseCVEResponse(response, null, null, null, null);
                sendMail(str);
            }else {
                System.out.println("Failed to fetch data: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	private void sendMail(List<VulnerabilityDataResponse> str) throws MessagingException {
				if(str.size()>0)
				{
		        MimeMessage message = mailSender.createMimeMessage();
		        MimeMessageHelper helper = new MimeMessageHelper(message, true);
		        List<String> emails=userRepository.findAllEmails();
		        String listCves=str.stream().map(x->x.getCveId()).collect(Collectors.joining(", "));
		        for(String email : emails) {
		            helper.setTo(email);
		            helper.setSubject("New Vulnerabilities in NVD");
		            helper.setText("Here are the list of CVEs:\n" + listCves);
		            mailSender.send(message);
		            System.out.println("sent mail");

		            Notification notification = new Notification();
		            notification.setEmail(email);
		            notification.setMessage("New CVEs: " + listCves);
		            notification.setTimestamp(LocalDateTime.now());
		            notificationRepository.save(notification);
		        }

				}
	}
}
