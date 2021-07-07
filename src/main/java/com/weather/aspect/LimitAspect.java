/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.weather.aspect;

import com.google.common.util.concurrent.RateLimiter;
import com.weather.annotation.Limit;
import com.weather.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * tps limit aop
 *
 * @author shenyuming
 */
@Slf4j
@Aspect
@Component
public class LimitAspect {

	private ConcurrentHashMap<String, RateLimiter> RATE_LIMITER  = new ConcurrentHashMap<>();

	private RateLimiter rateLimiter;

	@Pointcut("@annotation(com.weather.annotation.Limit)")
	public void limitPointCut() {}

	@Around("limitPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		Signature signature = point.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Object target = point.getTarget();
		Method currentMethod = target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
		Limit annotation = currentMethod.getAnnotation(Limit.class);
		double limitNum = annotation.limitNum();
		String functionName = methodSignature.getName();

		if (RATE_LIMITER.containsKey(functionName)) {
			rateLimiter = RATE_LIMITER.get(functionName);
		} else {
			RATE_LIMITER.put(functionName, RateLimiter.create(limitNum));
			rateLimiter = RATE_LIMITER.get(functionName);
		}

		if (rateLimiter.tryAcquire()) {
			log.info("success");
			return point.proceed();
		} else {
			throw new BizException("System is busy now, please try again later.");
		}
	}

}
