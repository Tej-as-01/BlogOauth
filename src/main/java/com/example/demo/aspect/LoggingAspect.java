package com.example.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	
	private static final Logger logger=LoggerFactory.getLogger(LoggingAspect.class);
	
	//Logs the method has started before the method execution starts
	@Before("execution(* com.example.demo.service.*.*(..))")
	public void logMethodStart(JoinPoint joinpoint)
	{logger.info("Method started: {}",joinpoint.getSignature().toShortString());
	
	}
	
	//Logs the completion of method after the method successfully returns 	
	@AfterReturning("execution(* com.example.demo.service.*.*(..))")
	public void logMethodEnd(JoinPoint joinpoint)
	{
		logger.info("Method finished: {}",joinpoint.getSignature().toShortString());
	}
	

}
