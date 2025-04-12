package com.tool.AutomatedRiskAssessment.dto;


import java.time.LocalDate;
import java.util.List;

public class VulnerabilitiesReportDTO {

    private String company;
    private String product;
    private String version;
    private String cveId;
    private String description;
    private double baseScore;
    private String severity;
    private LocalDate lastModified;
    private String references;
    private String type;

    // Constructor including assetId
    public VulnerabilitiesReportDTO(String company, String product, String version, String cveId,
                           String description, double baseScore, String severity,
                           LocalDate lastModified, String references, String type) {
        this.company = company;
        this.product = product;
        this.version = version;
        this.cveId = cveId;
        this.description = description;
        this.baseScore = baseScore;
        this.severity = severity;
        this.lastModified = lastModified;
        this.references = references;
        this.type = type;
    }

 // Getters and Setters for all fields including type
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    // Other getters and setters
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getCveId() { return cveId; }
    public void setCveId(String cveId) { this.cveId = cveId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getBaseScore() { return baseScore; }
    public void setBaseScore(double baseScore) { this.baseScore = baseScore; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public LocalDate getLastModified() { return lastModified; }
    public void setLastModified(LocalDate lastModified) { this.lastModified = lastModified; }

    public String getReferences() { return references; }
    public void setReferences(String references) { this.references = references; }
}
