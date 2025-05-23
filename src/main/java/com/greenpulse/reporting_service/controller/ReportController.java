package com.greenpulse.reporting_service.controller;

import com.greenpulse.reporting_service.dto.WeeklyWeatherSensorData;
import com.greenpulse.reporting_service.model.WeatherDataEvent;
import com.greenpulse.reporting_service.service.PdfReportService;
import com.greenpulse.reporting_service.service.RedisWeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final RedisWeatherService redisWeatherService;
    private final PdfReportService pdfReportService;


    @GetMapping("/weekly/{city}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<String> generateReport(@PathVariable String city) {
        try {
            WeeklyWeatherSensorData data = redisWeatherService.getWeatherAndSensorEvents(city);
            pdfReportService.generateReport(data, city);
            return ResponseEntity.ok("Report generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to generate report.");
        }
    }

    //TODO
//    @Scheduled(cron = "0 0 12 * * SUN") // каждое воскресенье в 12:00
//    public void generateWeeklyReports() throws IOException {
//        List<String> cities = List.of("Baku"); // Можно вынести в конфиг
//        for (String city : cities) {
//            WeeklyWeatherSensorData data = redisWeatherService.getWeatherAndSensorEvents(city);
//            pdfReportService.generateReport(data, city);
//        }
//    }
}
