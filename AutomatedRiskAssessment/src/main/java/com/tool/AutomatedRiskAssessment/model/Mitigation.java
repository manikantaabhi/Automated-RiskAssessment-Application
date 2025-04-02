package com.tool.AutomatedRiskAssessment.model;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "mitigation", uniqueConstraints = {@UniqueConstraint(columnNames = "cveId")})
public class Mitigation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Lob
    private String mitigationText;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(nullable = false, unique = true)  // Unique constraint (redundant but safe)
    private String cveId;

    public Mitigation() {}

    public Mitigation(String mitigationText, LocalDateTime time, String cveId) {
        this.mitigationText = mitigationText;
        this.time = time;
        this.cveId = cveId;
    }

    public String getCveId() { 
        return cveId;
    }

    public void setCveId(String cveId) {
        this.cveId = cveId;
    }
}
