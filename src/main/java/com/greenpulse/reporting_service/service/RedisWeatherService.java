package com.greenpulse.reporting_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenpulse.reporting_service.dto.WeeklyWeatherSensorData;
import com.greenpulse.reporting_service.model.SensorDataEvent;
import com.greenpulse.reporting_service.model.WeatherDataEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisWeatherService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public WeeklyWeatherSensorData getWeatherAndSensorEvents(String city) {
        Set<String> weatherKeys = redisTemplate.keys("weather-data:" + city + ":*");
        Set<String> sensorKeys = redisTemplate.keys("sensor-data:" + city + ":*");

        List<WeatherDataEvent> weatherResults = new ArrayList<>();
        List<SensorDataEvent> sensorResults = new ArrayList<>();

        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);

        if (weatherKeys != null) {
            for (String key : weatherKeys) {
                try {
                    String value = redisTemplate.opsForValue().get(key);
                    WeatherDataEvent event = objectMapper.readValue(value, WeatherDataEvent.class);
                    if (event.getTimestamp().isAfter(oneWeekAgo)) {
                        weatherResults.add(event);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing weather value for key: " + key);
                }
            }
        }

        if (sensorKeys != null) {
            for (String key : sensorKeys) {
                try {
                    String value = redisTemplate.opsForValue().get(key);
                    SensorDataEvent event = objectMapper.readValue(value, SensorDataEvent.class);
                    if (event.getTimestamp().isAfter(oneWeekAgo)) {
                        sensorResults.add(event);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing sensor value for key: " + key);
                }
            }
        }

        weatherResults.sort(Comparator.comparing(WeatherDataEvent::getTimestamp));
        sensorResults.sort(Comparator.comparing(SensorDataEvent::getTimestamp));

        return new WeeklyWeatherSensorData(weatherResults, sensorResults);
    }
}
