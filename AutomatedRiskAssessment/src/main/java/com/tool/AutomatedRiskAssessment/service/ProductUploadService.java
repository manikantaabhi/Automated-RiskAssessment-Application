package com.tool.AutomatedRiskAssessment.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tool.AutomatedRiskAssessment.model.OrganizationProducts;
import com.tool.AutomatedRiskAssessment.repo.OrganizationProductsReposotory;

@Service
public class ProductUploadService {

    private final OrganizationProductsReposotory repository;

    public ProductUploadService(OrganizationProductsReposotory repository) {
        this.repository = repository;
    }

    public List<OrganizationProducts> processExcelFile(MultipartFile file, String username) throws Exception {
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<OrganizationProducts> entries = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header

                String vendor = getCellValueAsString(row.getCell(0));
                String product = getCellValueAsString(row.getCell(1));
                String version = getCellValueAsString(row.getCell(2));

                if (!repository.findByVendorAndProductAndVersionAndUsername(vendor, product, version, username).isPresent()) {
                	
                	OrganizationProducts entry = new OrganizationProducts(vendor, product, version, username);
                	entries.add(entry);
                }
            }

            System.out.println(entries);
           return repository.saveAll(entries);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
		return null;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

}
