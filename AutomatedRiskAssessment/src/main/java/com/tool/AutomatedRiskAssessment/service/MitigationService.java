package com.tool.AutomatedRiskAssessment.service;

import com.tool.AutomatedRiskAssessment.model.Mitigation;
import com.tool.AutomatedRiskAssessment.model.Vulnerability;
import com.tool.AutomatedRiskAssessment.repo.MitigationRepository;
import com.tool.AutomatedRiskAssessment.repo.VulnerabilityRepository;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class MitigationService {


	    private final VulnerabilityRepository vulnerabilityRepository;
	    private final MitigationRepository mitigationRepository;
	    private final OkHttpClient client;

	    @Autowired
	    public MitigationService(VulnerabilityRepository vulnerabilityRepository, MitigationRepository mitigationRepository) {
	        this.vulnerabilityRepository = vulnerabilityRepository;
	        this.mitigationRepository = mitigationRepository;
	        this.client = new OkHttpClient.Builder()
	                .connectTimeout(60, TimeUnit.SECONDS)
	                .readTimeout(60, TimeUnit.SECONDS)
	                .writeTimeout(60, TimeUnit.SECONDS)
	                .build();
	    }

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";

    @Async
    public void processVulnerabilities() {
        List<Vulnerability> vulnerabilities = vulnerabilityRepository.findAll();
        for (Vulnerability vulnerability : vulnerabilities) {
            // Check if mitigation already exists for this CVE ID
            if (mitigationRepository.findByCveId(vulnerability.getCveId()).isEmpty()) {
                generateMitigationAsync(vulnerability);
            }
        }
    }

    @Async
    public CompletableFuture<Void> generateMitigationAsync(Vulnerability vulnerability) {
        String prompt = "Provide a short mitigation just in 2 lines for: " + vulnerability.getDescription()+" Please do not add any extra text like Here are details or headings etc, just give me exactly the mitigation";
        Request request = new Request.Builder()
                .url(OLLAMA_URL)
                .post(RequestBody.create(MediaType.parse("application/json"),
                        "{\"model\":\"llama3\",\"prompt\":\"" + prompt + "\"}"))
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                return CompletableFuture.completedFuture(null);
            }

            String responseBody = response.body().string(); // Read full response
            StringBuilder mitigationText = new StringBuilder();

            // Split response into JSON objects (assuming newline-separated JSON objects)
            for (String line : responseBody.split("\n")) {
                JSONObject jsonObject = new JSONObject(line);
                mitigationText.append(jsonObject.optString("response", ""));
            }

            String mitigationFinal = mitigationText.toString().trim();
            System.out.println(mitigationFinal);
            //System.out.println(mitigationFinal);
            if (!mitigationFinal.isEmpty()) {
                Mitigation mitigation = new Mitigation(mitigationFinal, LocalDateTime.now(), vulnerability.getCveId());
                if (mitigationRepository.findByCveId(vulnerability.getCveId()).size()==0) {
                	mitigationRepository.save(mitigation);
                }
                
            }
        } catch (IOException e) {
            System.out.println("Failed generating Mitigation for CVE : "+vulnerability.getCveId());
        }
        return CompletableFuture.completedFuture(null);
    }

}
