package com.tool.AutomatedRiskAssessment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private NvdDataService nvdDataService;

    @Override
    public void run(String... args) {
        System.out.println("Fetching data from NVD...");
        //nvdDataService.fetchAndSaveProducts();
        System.out.println("NVD data update completed.");
    }
}
