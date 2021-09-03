package com.awesome.infrastructures.excutionlog;

import com.awesome.domains.log.entities.LogDAO;
import com.awesome.domains.log.entities.LogEntity;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class AwesomeAspect {
    private LogDAO logDao;

    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        LogEntity logEntity = new LogEntity();
        logEntity.setSignature(joinPoint.getSignature().getName());
        logEntity.setExcutionTime(executionTime);

        logDao.save(logEntity);

        return proceed;
    }
}
