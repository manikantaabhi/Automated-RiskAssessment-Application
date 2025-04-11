package com.tool.AutomatedRiskAssessment.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tool.AutomatedRiskAssessment.model.OrganizationProducts;

public interface OrganizationProductsReposotory extends JpaRepository<OrganizationProducts, Long> {
	Optional<OrganizationProducts> findByVendorAndProductAndVersionAndUsername(String vendor, String product, String version, String username);

}
