package com.greenpulse.reporting_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    public List<WeatherDataEvent> getEventsLastWeek(String city) {
        Set<String> keys = redisTemplate.keys("weather-data:" + city + ":*");
        System.out.println("Found keys: " + keys);
        if (keys == null) return List.of();

        List<WeatherDataEvent> results = new ArrayList<>();
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);

        for (String key : keys) {
            try {
                String value = redisTemplate.opsForValue().get(key);
                WeatherDataEvent event = objectMapper.readValue(value, WeatherDataEvent.class);
                if (event.getTimestamp().isAfter(oneWeekAgo)) {
                    results.add(event);
                }
            } catch (Exception e) {
                System.err.println("Error parsing value for key " + key);
            }
        }

        return results.stream()
                .sorted(Comparator.comparing(WeatherDataEvent::getTimestamp))
                .toList();
    }
}
