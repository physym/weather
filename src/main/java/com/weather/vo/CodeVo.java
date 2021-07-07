package com.weather.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeVo implements Serializable {

    private static final long serialVersionUID = -1717778219564300364L;

    private String code;

    private String name;
}
