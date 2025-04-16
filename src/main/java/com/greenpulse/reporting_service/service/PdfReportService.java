package com.greenpulse.reporting_service.service;

import com.greenpulse.reporting_service.model.WeatherDataEvent;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfReportService {

    public void generateReport(List<WeatherDataEvent> events, String city) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream content = new PDPageContentStream(document, page)) {
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.setLeading(14.5f);
            content.newLineAtOffset(50, 750);
            content.showText("Weekly Weather Report for " + city);
            content.newLine();
            content.newLine();

            for (WeatherDataEvent event : events) {
                String line = event.getTimestamp() + " | Temp: " + event.getTemperature() + "°C | Humidity: " + event.getHumidity() + "%";
                content.showText(line);
                content.newLine();
            }

            content.endText();
        }

        String reportDir = "reports";
        new File(reportDir).mkdirs();

        String fileName = reportDir + "/weather-report-" + city + "-" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".pdf";
        document.save(fileName);
        document.close();

        System.out.println("PDF Report saved as " + fileName);
    }
}
