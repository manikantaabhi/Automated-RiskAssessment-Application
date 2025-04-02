package com.tool.AutomatedRiskAssessment.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool.AutomatedRiskAssessment.model.VulnerabilityDetails;
import com.tool.AutomatedRiskAssessment.repo.VulnerabilityDetailsRepository;

@Service
public class ReportService {

    @Autowired
    private VulnerabilityDetailsRepository vulnerabilityDetailsRepo;

    public byte[] generateExcel() throws IOException {
        List<VulnerabilityDetails> vulnerabilities = vulnerabilityDetailsRepo.findAll();

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Your ORG Vulnerabilities");
        HSSFRow row = sheet.createRow(0);

        // Creating headers
        row.createCell(0).setCellValue("CVE_ID");
        row.createCell(1).setCellValue("COMPANY");
        row.createCell(2).setCellValue("PRODUCT");
        row.createCell(3).setCellValue("VERSION");
        row.createCell(4).setCellValue("DESCRIPTION");
        row.createCell(5).setCellValue("BASE SCORE");
        row.createCell(6).setCellValue("SEVERITY");
        row.createCell(7).setCellValue("LAST MODIFIED");
        row.createCell(8).setCellValue("TYPE");
        row.createCell(9).setCellValue("REFERENCES");

        // Writing data rows
        int dataRowIndex = 1;

        for (VulnerabilityDetails vulnerability : vulnerabilities) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex++);
            dataRow.createCell(0).setCellValue(vulnerability.getCveId());
            dataRow.createCell(1).setCellValue(vulnerability.getCompany());
            dataRow.createCell(2).setCellValue(vulnerability.getProduct());
            dataRow.createCell(3).setCellValue(vulnerability.getVersion());
            dataRow.createCell(4).setCellValue(vulnerability.getDescription());
            dataRow.createCell(5).setCellValue(vulnerability.getBaseScore());
            dataRow.createCell(6).setCellValue(vulnerability.getSeverity());
            dataRow.createCell(7).setCellValue(vulnerability.getLastModified().toString());
            dataRow.createCell(8).setCellValue(vulnerability.getType());
            dataRow.createCell(9).setCellValue(vulnerability.getReferences());
        }

        // Return workbook as a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
}
