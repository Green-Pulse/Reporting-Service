package com.greenpulse.reporting_service.dto;

import com.greenpulse.reporting_service.model.SensorDataEvent;
import com.greenpulse.reporting_service.model.WeatherDataEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class WeeklyWeatherSensorData {
    private List<WeatherDataEvent> weatherEvents;
    private List<SensorDataEvent> sensorEvents;
}
