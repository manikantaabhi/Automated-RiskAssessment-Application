package com.tool.AutomatedRiskAssessment.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    
    @Column(nullable=false, unique = true)
    private String cveId;

    private String source;
    private Double score;
    private String severity;
    
    
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	public String getCveId() {
		return cveId;
	}

	public void setCveId(String cveId) {
		this.cveId = cveId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public Metric() {
    	
    }

    public Metric(String source, Double score, String severity, String cveId) {
        this.source = source;
        this.score = score;
        this.severity = severity;
        this.cveId = cveId;
    }
}
