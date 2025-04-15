package com.tool.AutomatedRiskAssessment.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tool.AutomatedRiskAssessment.repo.SuggestionRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/suggestions")
public class SuggestionController {

    @Autowired
    private SuggestionRepository repo;

    @GetMapping("/{type}")
    public List<String> getSuggestions(
            @PathVariable String type,
            @RequestParam("q") String query,
            @RequestParam(value = "make", required = false) String make,
            @RequestParam(value = "product", required = false) String product) {

        switch (type) {
            case "make":
                return repo.findDistinctVendorByVendorContainingIgnoreCase(query);
            case "product":
                if (make != null) {
                    return repo.findDistinctProductByVendorAndProductContaining(make, query);
                }
                return repo.findDistinctProductByProductContainingIgnoreCase(query);
            case "version":
                if (make != null && product != null) {
                    return repo.findDistinctVersionByVendorAndProductAndVersionContaining(make, product, query);
                }
                return repo.findDistinctVersionByVersionContainingIgnoreCase(query);
            default:
                return Collections.emptyList();
        }
    }

}
