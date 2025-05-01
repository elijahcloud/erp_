package com.vdt.vdt.controller;


import com.lowagie.text.DocumentException;
import com.vdt.vdt.service.SlaComplianceExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


@RestController
@RequestMapping("/api/reports")
public class SlaComplianceReportController {

    private final SlaComplianceExportService exportService;

    private SlaComplianceReportController(SlaComplianceExportService exportService) {
        this.exportService = exportService;
    }


    @GetMapping("/download/csv")
    public ResponseEntity<byte[]> downloadCsvReport() throws IOException {
        String fileName = "SLA_Compliance_Report.csv";
        exportService.exportSlaComplianceToCsv(fileName);

        File file = new File(fileName);
        InputStream inputStream = new FileInputStream(file);

        byte[] data = inputStream.readAllBytes();
        inputStream.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .header(HttpHeaders.CONTENT_TYPE, "application/csv")
                .body(data);
    }

    @GetMapping("/download/pdf")
    public ResponseEntity<byte[]> downloadPdfReport() throws IOException, DocumentException {
        String fileName = "SLA_Compliance_Report.pdf";
        exportService.exportSlaComplianceToPdf(fileName);

        File file = new File(fileName);
        InputStream inputStream = new FileInputStream(file);

        byte[] data = inputStream.readAllBytes();
        inputStream.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(data);
    }
}

