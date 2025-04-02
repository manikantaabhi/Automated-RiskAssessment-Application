package com.tool.AutomatedRiskAssessment.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.*;

import com.tool.AutomatedRiskAssessment.model.Metric;
import com.tool.AutomatedRiskAssessment.model.Vulnerability;

public interface MetricRepository extends JpaRepository<Metric, Long> {
	@Query("SELECT m.severity FROM Metric m WHERE m.cveId = :cveId")
	String findSeverityByCveId(@Param("cveId") String cveId);
}
