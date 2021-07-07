package com.weather.service;

import java.util.Optional;

/**
 * Created by shenyuming
 * 2021-07-07 11:33
 */
public interface WeatherService {

    /**
     * get temperature
     * @param province
     * @param city
     * @param country
     * @return
     */
    Optional<Integer> getTemperature(String province, String city, String country);
}
