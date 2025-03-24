package com.tool.AutomatedRiskAssessment.repo;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tool.AutomatedRiskAssessment.model.Metric;
import com.tool.AutomatedRiskAssessment.model.Vulnerability;

public interface MetricRepository extends JpaRepository<Metric, Long> {

	Metric findByVulnerabilityAndSource(Vulnerability vulnerability, String string);
}
