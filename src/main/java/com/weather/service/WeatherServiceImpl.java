package com.weather.service;

import com.alibaba.fastjson.JSONObject;
import com.weather.exception.BizException;
import com.weather.vo.CodeVo;
import com.weather.vo.WeatherVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by shenyuming
 * 2021-07-07 11:33
 */
@Slf4j
@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * get temperature
     * @param province
     * @param city
     * @param country
     * @return
     */
    @Override
    @Retryable(value = BizException.class, maxAttempts = 5, backoff = @Backoff(multiplier = 1))
    public Optional<Integer> getTemperature(String province, String city, String country) {
        log.info("get allProvinceCode");
        Map<String, String> provinceCodeMap = this.getProvinceCode();
        String provinceCode = provinceCodeMap.get(province);
        if (null == provinceCode) {
            throw new BizException("invalid parameter: province");
        }

        log.info("get cityCode");
        Map<String, String> cityCodeMap = this.getCityCode(provinceCode);
        String cityCode = cityCodeMap.get(city);
        if (null == cityCode) {
            throw new BizException("invalid parameter: city");
        }

        log.info("get countryCode");
        Map<String, String> countryCodeMap = this.getCountryCode(provinceCode + cityCode);
        String countryCode = countryCodeMap.get(country);
        if (null == countryCode) {
            throw new BizException("invalid parameter: country");
        }

        log.info("get and return temperature");
        return this.getWeather(provinceCode + cityCode + countryCode);
    }

    /**
     * Get the province code of China
     * @return
     */
    private Map<String, String> getProvinceCode() {
        String url = "http://www.weather.com.cn/data/city3jdata/china.html";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        List<CodeVo> list = new ArrayList<>();
        try {
            ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
            if (!HttpStatus.OK.equals(resEntity.getStatusCode())) {
                throw new BizException(resEntity.getStatusCode().getReasonPhrase());
            }
            JSONObject jsonObject = JSONObject.parseObject(resEntity.getBody());
            jsonObject.entrySet().forEach(stringObjectEntry -> {
                CodeVo vo = CodeVo.builder()
                        .code(stringObjectEntry.getKey())
                        .name(stringObjectEntry.getValue().toString())
                        .build();
                list.add(vo);
            });
        } catch (Exception e) {
            throw new BizException(e.getMessage());
        }
        Map<String, String> provinceMap = list.stream().collect(Collectors.toMap(CodeVo::getName, CodeVo::getCode));
        return provinceMap;
    }

    /**
     * Get the city code of one certain province
     * @return
     */
    private Map<String, String> getCityCode(String provinceCode) {
        String url = "http://www.weather.com.cn/data/city3jdata/provshi/" + provinceCode + ".html";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        List<CodeVo> list = new ArrayList<>();
        try {
            ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
            if (!HttpStatus.OK.equals(resEntity.getStatusCode())) {
                throw new BizException(resEntity.getStatusCode().getReasonPhrase());
            }
            JSONObject jsonObject = JSONObject.parseObject(resEntity.getBody());
            jsonObject.entrySet().forEach(stringObjectEntry -> {
                CodeVo vo = CodeVo.builder()
                        .code(stringObjectEntry.getKey())
                        .name(stringObjectEntry.getValue().toString())
                        .build();
                list.add(vo);
            });
        } catch (Exception e) {
            throw new BizException(e.getMessage());
        }
        Map<String, String> cityMap = list.stream().collect(Collectors.toMap(CodeVo::getName, CodeVo::getCode));
        return cityMap;
    }

    /**
     * Get the county code of one certain city
     * @return
     */
    private Map<String, String> getCountryCode(String provinceCityCode) {
        String url = "http://www.weather.com.cn/data/city3jdata/station/" + provinceCityCode + ".html";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        List<CodeVo> list = new ArrayList<>();
        try {
            ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
            if (!HttpStatus.OK.equals(resEntity.getStatusCode())) {
                throw new BizException(resEntity.getStatusCode().getReasonPhrase());
            }
            JSONObject jsonObject = JSONObject.parseObject(resEntity.getBody());
            jsonObject.entrySet().forEach(stringObjectEntry -> {
                CodeVo vo = CodeVo.builder()
                        .code(stringObjectEntry.getKey())
                        .name(stringObjectEntry.getValue().toString())
                        .build();
                list.add(vo);
            });
        } catch (Exception e) {
            throw new BizException(e.getMessage());
        }
        Map<String, String> countryMap = list.stream().collect(Collectors.toMap(CodeVo::getName, CodeVo::getCode));
        return countryMap;
    }

    /**
     * Get the weather of one certain county
     * @return
     */
    private Optional<Integer> getWeather(String code) {
        String url = "http://www.weather.com.cn/data/sk/" + code + ".html";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        Integer temp;
        try {
            ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
            if (!HttpStatus.OK.equals(resEntity.getStatusCode())) {
                throw new BizException(resEntity.getStatusCode().getReasonPhrase());
            }
            JSONObject jsonObject = JSONObject.parseObject(resEntity.getBody());
            WeatherVo weatherVo = JSONObject.parseObject(jsonObject.get("weatherinfo").toString(), WeatherVo.class);
            temp = Math.round(Float.valueOf(weatherVo.getTemp()));
        } catch (Exception e) {
            throw new BizException(e.getMessage());
        }
        return Optional.of(temp);
    }
}
