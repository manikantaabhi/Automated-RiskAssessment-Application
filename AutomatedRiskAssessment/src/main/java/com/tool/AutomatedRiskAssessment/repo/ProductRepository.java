package com.tool.AutomatedRiskAssessment.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tool.AutomatedRiskAssessment.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Custom query methods if needed
    Product findByVendorAndProductAndVersion(String vendor, String product, String version);

    boolean existsByCpe(String cpe);
}
