package com.weather.controller;

import com.weather.JsonResult;
import com.weather.annotation.Limit;
import com.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by shenyuming
 * 2021-07-07 11:33
 */
@RestController
@RequestMapping("/api")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    /**
     * fetch and return the temperature of one certain county in China
     */
    @GetMapping("/temperature")
    @Limit
    public JsonResult<String> getTemperature(@RequestParam("province") String province,
                                             @RequestParam("city") String city,
                                             @RequestParam("country") String country) {
        Integer temp = weatherService.getTemperature(province, city, country).get();
        return JsonResult.ok("success",
                "The current temperature of " + province + city + country + " is " + temp + " ℃");
    }

    /**
     * test limit
     */
    @GetMapping("/limit")
    @Limit
    public JsonResult<String> testLimit() {
        return JsonResult.okNoData("success");
    }

}
