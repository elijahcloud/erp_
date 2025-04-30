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

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Service

public class SlaComplianceExportService {

    private final TicketRepository ticketRepository;

    public SlaComplianceExportService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void exportSlaComplianceToCsv(String fileName) throws IOException {
        List<Object[]> slaComplianceData = ticketRepository.getSlaComplianceData(); // Assume this query fetches SLA data

        try (Writer writer = new FileWriter(fileName);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Ticket ID", "Title", "Status", "Priority", "SLA Breach", "Resolved At", "First Response At", "Created At"))) {

            for (Object[] record : slaComplianceData) {
                csvPrinter.printRecord(record);
            }

            csvPrinter.flush();
        }
    }
    public void exportSlaComplianceToPdf(String fileName) throws DocumentException, IOException {
        List<Object[]> slaComplianceData = ticketRepository.getSlaComplianceData(); // Assume this query fetches SLA data

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
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
            table.addCell((String) record[0]);
            table.addCell((String) record[1]);
            table.addCell((String) record[2]);
            table.addCell((String) record[3]);
            table.addCell((String) record[4]);
            table.addCell((String) record[5]);
            table.addCell((String) record[6]);
        }

        document.add(table);
        document.close();
    }
}

