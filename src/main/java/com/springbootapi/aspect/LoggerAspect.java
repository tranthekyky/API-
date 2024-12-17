package com.springbootapi.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.logging.Logger;



@Component
@Aspect
public class LoggerAspect {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Pointcut("execution(* com.springbootapi.controller.ProductController.*(..))")
    public void controllerPointcut() {}
    @Pointcut("execution(* com.springbootapi.service.product_impl.ProductService.*(..))")
    public void servicePointcut() {}

    @Around("controllerPointcut()")
    public Object logMethodController(ProceedingJoinPoint joinPoint) throws Throwable {
        Long startTime = System.currentTimeMillis();
       String method = joinPoint.getSignature().getName();
       String className = joinPoint.getTarget().getClass().getName();
       String ip = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr();
        logger.info("-------------");
        logger.info("Class Name : " + className);
        logger.info("Method Name :  "  +method);
        logger.info("Client IP : " + ip);

        Object result = joinPoint.proceed();
        Long datetime = System.currentTimeMillis() - startTime;
        logger.info("Executed successfully !");
        logger.info("Execution Time : " + datetime);
        logger.info("------------");

        return result;
    }
}
