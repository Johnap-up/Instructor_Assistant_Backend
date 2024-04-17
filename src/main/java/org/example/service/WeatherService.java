package org.example.service;

import org.example.entity.dto.homepage.WeatherVO;

public interface WeatherService {
    WeatherVO fetchWeather(double longitude, double latitude);
}
