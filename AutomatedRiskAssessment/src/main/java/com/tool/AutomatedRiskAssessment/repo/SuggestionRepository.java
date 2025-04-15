package com.tool.AutomatedRiskAssessment.repo;

import com.tool.AutomatedRiskAssessment.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuggestionRepository extends JpaRepository<Product, Long> {

    // Query only the vendor field and return a list of Strings
    @Query("SELECT DISTINCT p.vendor FROM Product p WHERE p.vendor LIKE %?1%")
    List<String> findDistinctVendorByVendorContainingIgnoreCase(String query);

    // Query only the product field and return a list of Strings
    @Query("SELECT DISTINCT p.product FROM Product p WHERE p.product LIKE %?1%")
    List<String> findDistinctProductByProductContainingIgnoreCase(String query);

    // Query only the version field and return a list of Strings
    @Query("SELECT DISTINCT p.version FROM Product p WHERE p.version LIKE %?1%")
    List<String> findDistinctVersionByVersionContainingIgnoreCase(String query);
    
    @Query("SELECT DISTINCT p.product FROM Product p WHERE LOWER(p.vendor) = LOWER(?1) AND p.product LIKE %?2%")
    List<String> findDistinctProductByVendorAndProductContaining(String vendor, String query);

    @Query("SELECT DISTINCT p.version FROM Product p WHERE LOWER(p.vendor) = LOWER(?1) AND LOWER(p.product) = LOWER(?2) AND p.version LIKE %?3%")
    List<String> findDistinctVersionByVendorAndProductAndVersionContaining(String vendor, String product, String query);

    
}
