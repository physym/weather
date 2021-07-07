/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.weather.annotation;

import java.lang.annotation.*;

/**
 * limit annotation
 *
 * @author shenyuming
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Limit {

	double limitNum() default 100;
}
