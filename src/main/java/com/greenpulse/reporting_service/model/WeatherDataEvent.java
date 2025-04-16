package com.greenpulse.reporting_service.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDataEvent {

    private String city;
    private double temperature;
    private double humidity;
    private LocalDateTime timestamp;

}

