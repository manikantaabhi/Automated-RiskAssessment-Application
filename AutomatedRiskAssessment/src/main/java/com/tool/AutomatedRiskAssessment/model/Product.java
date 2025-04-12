package com.tool.AutomatedRiskAssessment.model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vendor;
    private String product;
    private String version;

    @Column(length = 512)
    private String cpe;

    public Product() {
    }

    public Product(String vendor, String product, String version, String cpe) {
        this.vendor = vendor;
        this.product = product;
        this.version = version;
        this.cpe = cpe;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCpe() {
        return cpe;
    }

    public void setCpe(String cpe) {
        this.cpe = cpe;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", vendor='" + vendor + '\'' +
                ", product='" + product + '\'' +
                ", version='" + version + '\'' +
                ", cpe='" + cpe + '\'' +
                '}';
    }
}
