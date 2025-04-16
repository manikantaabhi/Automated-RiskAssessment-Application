package com.tool.AutomatedRiskAssessment.repo;

import com.tool.AutomatedRiskAssessment.model.OrganizationProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrganizationProductsReposotory extends JpaRepository<OrganizationProducts, Long> {

    // Fetch all distinct (vendor, product) pairs (ignore version)
    @Query("SELECT DISTINCT o.vendor, o.product FROM OrganizationProducts o")
    List<Object[]> findDistinctVendorProductPairs();

    // Fetch usernames for a specific vendor and product
    @Query("SELECT o.username FROM OrganizationProducts o WHERE o.vendor = :vendor AND o.product = :product")
    List<String> findUsernamesByVendorAndProduct(String vendor, String product);

    // Optional: Find all products uploaded by a specific user
    List<OrganizationProducts> findByUsername(String username);
    
    Optional<OrganizationProducts> findByVendorAndProductAndVersionAndUsername(String vendor, String product, String version, String username);

}
