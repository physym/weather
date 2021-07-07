package com.weather.service;

import com.weather.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@SpringBootTest
class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    @Test
    void getTemperature() {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        Optional<Integer> opt = weatherService.getTemperature("江苏", "苏州", "吴中");
        Assertions.assertTrue(pattern.matcher(opt.get().toString()).matches());
    }

    @Test
    void getTemperatureThrow1() {
        Assertions.assertThrows(BizException.class, () -> {
            Optional<Integer> opt = weatherService.getTemperature("江苏1", "苏州", "吴中");
            log.info("temp:" + opt.get());
        });
    }

    @Test
    void getTemperatureThrow2() {
        Assertions.assertThrows(BizException.class, () -> {
            Optional<Integer> opt = weatherService.getTemperature("江苏", "苏州1", "吴中");
            log.info("temp:" + opt.get());
        });
    }

    @Test
    void getTemperatureThrow3() {
        Assertions.assertThrows(BizException.class, () -> {
            Optional<Integer> opt = weatherService.getTemperature("江苏", "苏州", "吴中1");
            log.info("temp:" + opt.get());
        });
    }

}