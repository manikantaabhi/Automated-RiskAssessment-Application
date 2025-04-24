package com.tool.AutomatedRiskAssessment.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationProducts {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vendor;
    private String product;
    private String version;
    private String username;
	public OrganizationProducts(String vendor, String product, String version, String userName) {
		super();
		this.vendor = vendor;
		this.product = product;
		this.version = version;
		this.username = userName;
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
	public String getUserName() {
		return username;
	}
	public void setUserName(String userName) {
		this.username = userName;
	}
	@Override
	public String toString() {
		return "OrganizationProducts [id=" + id + ", vendor=" + vendor + ", product=" + product + ", version=" + version
				+ ", userName=" + username + "]";
	}
    
    
}
