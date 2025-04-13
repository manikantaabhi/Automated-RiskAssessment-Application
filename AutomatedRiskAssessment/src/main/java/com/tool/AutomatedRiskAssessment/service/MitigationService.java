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
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class MitigationService {


	    private final VulnerabilityRepository vulnerabilityRepository;
	    private final MitigationRepository mitigationRepository;
	    private final OkHttpClient client;
	    private boolean error=false;
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
            if (mitigationRepository.findByCveId(vulnerability.getCveId())==null) {
            	if(!error)
            		generateMitigationAsync(vulnerability);
            }
        }
    }
    public String processVulnerabilities(String cveId) {
            if (mitigationRepository.findByCveId(cveId)==null) {
            	Optional<Vulnerability> x=vulnerabilityRepository.findByCveId(cveId);
            	if(x.isPresent())
            		return generateMitigationAsync(x.get());
            	else {
					System.out.println("No cveId in Vulnerability table :"+cveId);
				}
        }
			return "Not Found";
    }

    public String generateMitigationAsync(Vulnerability vulnerability) {
    	System.out.println("generating...");
        String prompt = "Provide a short mitigation just in 2 lines for: " + vulnerability.getDescription()
                + " Please do not add any extra text like Here are details or headings etc, just give me exactly the mitigation";

        Request request = new Request.Builder()
                .url(OLLAMA_URL)
                .post(RequestBody.create(MediaType.parse("application/json"),
                        "{\"model\":\"llama3\",\"prompt\":\"" + prompt + "\"}"))
                .addHeader("Accept", "application/json")
                .build();

        try {
            try (Response response = executeWithRetry(request, 3)) {

                if (!response.isSuccessful() || response.body() == null) {
                    return null;
                }

                String responseBody = response.body().string();
                StringBuilder mitigationText = new StringBuilder();
                for (String line : responseBody.split("\n")) {
                    JSONObject jsonObject = new JSONObject(line);
                    mitigationText.append(jsonObject.optString("response", ""));
                }

                String mitigationFinal = mitigationText.toString().trim();
                if (!mitigationFinal.isEmpty()) {
                    Mitigation mitigation = new Mitigation(mitigationFinal, LocalDateTime.now(), vulnerability.getCveId());
                    if (mitigationRepository.findByCveId(vulnerability.getCveId()) == null) {
                        mitigationRepository.save(mitigation);
                    }
                    System.out.println("generated.");
                    return mitigationFinal;
                    
                }
                else
                {
                	System.out.println("empty");
                }
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Timeout while calling Ollama API for CVE: " + vulnerability.getCveId());
            error=true;
            e.printStackTrace();
        } catch (IOException e) {
        	error=true;
            System.err.println("IO error during Ollama API call: " + e.getMessage());
            processVulnerabilities();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    private Response executeWithRetry(Request request, int maxRetries) throws IOException {
        int attempt = 1;
        while (attempt < maxRetries) {
            try {
                return client.newCall(request).execute();
            } catch (SocketTimeoutException e) {
                attempt++;
                if (attempt >= maxRetries) throw e;
            }
        }
        throw new IOException("Failed after retries");
    }


}
