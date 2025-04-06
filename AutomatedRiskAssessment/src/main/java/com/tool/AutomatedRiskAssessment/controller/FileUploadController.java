package com.tool.AutomatedRiskAssessment.controller;

import com.tool.AutomatedRiskAssessment.model.ScheduledJob;
import com.tool.AutomatedRiskAssessment.repo.ScheduledJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class FileUploadController {

    @Autowired
    private ScheduledJobRepository scheduledJobRepository;

    @GetMapping("/uploadFile")
    public String testEndpoint() {
        return "Use POST /uploadFile to upload a file.";
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<Map<String, Object>> handleFileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId) {

        int count = 0;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean headerSkipped = false;
            while ((line = reader.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    System.out.println("Header: " + line);
                    continue;
                }
                System.out.println("Processing line: " + line);
                String[] values = line.split(",");
                if (values.length < 3) {
                    System.out.println("Skipping invalid row: " + line);
                    continue;
                }
                String vendor = values[0].trim();
                String productName = values[1].trim();
                String version = values[2].trim();
                String keywords = values.length >= 4 ? values[3].trim() : "";

                System.out.println("Parsed row: vendor=" + vendor +
                        ", productName=" + productName +
                        ", version=" + version +
                        ", keywords=" + keywords);

                ScheduledJob job = new ScheduledJob();
                job.setVendor(vendor);
                job.setProductName(productName);
                job.setVersion(version);
                job.setKeywords(keywords);
                job.setJobName("Uploaded Job for " + vendor + " " + productName);
                job.setStatus(ScheduledJob.JobStatus.SCHEDULED);
                // Set the user ID from the request
                job.setUserId(Long.valueOf(userId));

                scheduledJobRepository.save(job);
                System.out.println("Saved job for vendor: " + vendor + ", product: " + productName);
                count++;
            }
        } catch (Exception e) {
            System.out.println("Error processing file: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error processing file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Successfully uploaded " + count + " scheduled jobs.");
        return ResponseEntity.ok(response);
    }
}