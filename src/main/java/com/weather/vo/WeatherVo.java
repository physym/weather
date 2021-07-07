package com.weather.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherVo implements Serializable {

    private static final long serialVersionUID = -1717778219984300364L;

    private String isRadar;
    private String temp;
    private String city;
    private String WSE;
    private String njd;
    private String cityid;
    private String WD;
    private String AP;
    private String SD;
    private String Radar;
    private String sm;
    private String time;
    private String WS;
}
