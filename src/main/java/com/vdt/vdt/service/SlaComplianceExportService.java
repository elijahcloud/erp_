package com.vdt.vdt.service;



import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import com.vdt.vdt.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service

public class SlaComplianceExportService {

    private final TicketRepository ticketRepository;

    public SlaComplianceExportService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public byte[] exportSlaComplianceToCsv() throws IOException {
        List<Object[]> slaComplianceData = ticketRepository.getSlaComplianceData();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (Writer writer = new OutputStreamWriter(out);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                     "Ticket ID", "Title", "Status", "Priority", "SLA Breach", "Resolved At", "First Response At", "Created At"))) {

            for (Object[] record : slaComplianceData) {
                csvPrinter.printRecord(record);
            }

            csvPrinter.flush();
        }

        return out.toByteArray();
    }

    public byte[] exportSlaComplianceToPdf() throws DocumentException {
        List<Object[]> slaComplianceData = ticketRepository.getSlaComplianceData();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        Table table = new Table(7);
        table.addCell("Ticket ID");
        table.addCell("Ticket Title");
        table.addCell("Status");
        table.addCell("Priority");
        table.addCell("SLA Breach");
        table.addCell("Resolved At");
        table.addCell("Created At");

        for (Object[] record : slaComplianceData) {
            table.addCell(record[0] != null ? record[0].toString() : "");
            table.addCell(record[1] != null ? record[1].toString() : "");
            table.addCell(record[2] != null ? record[2].toString() : "");
            table.addCell(record[3] != null ? record[3].toString() : "");
            table.addCell(record[4] != null ? record[4].toString() : "");
            table.addCell(record[5] != null ? record[5].toString() : "");
            table.addCell(record[6] != null ? record[6].toString() : "");
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }


}

