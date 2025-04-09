package com.tool.AutomatedRiskAssessment.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tool.AutomatedRiskAssessment.dto.VulnerabilityDataResponse;
import com.tool.AutomatedRiskAssessment.model.Product;
import com.tool.AutomatedRiskAssessment.repo.ProductRepository;

@Service
public class NvdDataService {


    @Autowired
    private ProductRepository productRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String CPE_API_URL = "https://services.nvd.nist.gov/rest/json/cves/2.0";

    public void fetchAndSaveProducts() {

        Map<String, Object> response = restTemplate.getForObject(CPE_API_URL, Map.class);
        //System.out.println(response);
        if (response == null) {
            System.out.println("No products found");
            return;
        }
        List<Product> productList = new ArrayList<Product>();
        List<Map<String, Object>> cveItems = (List<Map<String, Object>>) response.get("vulnerabilities");
        List<VulnerabilityDataResponse> responses = new ArrayList<>();

        for (Map<String, Object> cveItem : cveItems) {
            Map<String, Object> cveData = (Map<String, Object>) cveItem.get("cve");
            List<Map<String, Object>> configurations = (List<Map<String, Object>>) cveData.get("configurations");
            if(configurations!=null)
            for (Map<String, Object> conf : configurations) {
                List<Map<String, Object>> nodes = (List<Map<String, Object>>) conf.get("nodes");

                for (Map<String, Object> node : nodes) {
                    List<Map<String, Object>> cpeMatches = (List<Map<String, Object>>) node.get("cpeMatch");

                    for (Map<String, Object> cpe : cpeMatches) {
                        String cpeString = (String) cpe.get("criteria"); // or "matchCriteriaId" in newer specs
                        //System.out.println("CPE: " + cpeString);
                        String[] parts = cpeString.split(":");
                        String vendor = parts[3];
                        String product = parts[4];
                        String version = parts[5];
                        if (!productRepository.existsByCpe(cpeString)) {
                            Product newProduct = new Product(vendor, product, version, cpeString);
                            productList.add(newProduct);
                        }
                    }
                }
            }
        }
        productRepository.saveAll(productList);
        System.out.println("Finished storing products ");
    }
}
