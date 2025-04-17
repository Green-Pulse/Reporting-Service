package com.greenpulse.reporting_service.service;

import com.greenpulse.reporting_service.dto.WeeklyWeatherSensorData;
import com.greenpulse.reporting_service.model.SensorDataEvent;
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

    public void generateReport(WeeklyWeatherSensorData data, String city) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream content = new PDPageContentStream(document, page)) {
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 14);
            content.setLeading(16f);
            content.newLineAtOffset(50, 750);
            content.showText("Weekly Weather & Sensor Report for " + city);
            content.newLine();
            content.newLine();

            content.setFont(PDType1Font.HELVETICA, 12);
            content.showText("Weather Data:");
            content.newLine();

            for (WeatherDataEvent event : data.getWeatherEvents()) {
                content.showText(event.getTimestamp() + " | Temp: " + event.getTemperature() + "°C | Humidity: " + event.getHumidity() + "%");
                content.newLine();
            }

            content.newLine();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.showText("Sensor Data:");
            content.newLine();

            for (SensorDataEvent event : data.getSensorEvents()) {
                content.showText(String.format("%s | Temp: %.1f | Pres: %.1f | Dewp: %.1f | Rain: %.1f | WS: %.1f | NE: %b | NW: %b | SE: %b",
                        event.getTimestamp(), event.getTemp(), event.getPres(), event.getDewp(),
                        event.getRain(), event.getWindSpeed(),
                        event.isWd_NE(), event.isWd_NW(), event.isWd_SE()));
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
