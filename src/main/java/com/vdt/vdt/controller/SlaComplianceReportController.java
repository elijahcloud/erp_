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
        byte[] data = exportService.exportSlaComplianceToCsv();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=SLA_Compliance_Report.csv")
                .header(HttpHeaders.CONTENT_TYPE, "application/csv")
                .body(data);
    }

    @GetMapping("/download/pdf")
    public ResponseEntity<byte[]> downloadPdfReport() throws IOException, DocumentException {
        byte[] data = exportService.exportSlaComplianceToPdf();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=SLA_Compliance_Report.pdf")
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(data);
    }

}

