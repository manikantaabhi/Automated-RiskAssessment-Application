package com.tool.AutomatedRiskAssessment.repo;

import com.tool.AutomatedRiskAssessment.model.Mitigation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MitigationRepository extends JpaRepository<Mitigation, Long> {
	List<Mitigation> findByCveId(String cveId);

}
