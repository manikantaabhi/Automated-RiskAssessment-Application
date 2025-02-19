package com.tool.AutomatedRiskAssessment.dto;

import lombok.Data;

@Data
public class CheckVulnerabilitiesRequest {

	private String softwareName;
	private String version;
	private String cveId;
	public String getSoftwareName() {
		return softwareName;
	}
	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCveId() {
		return cveId;
	}
	public void setCveId(String cveId) {
		this.cveId = cveId;
	}
	
}
