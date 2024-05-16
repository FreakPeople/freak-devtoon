package yjh.devtoon.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Configuration
public class LoggingAspect {

    @AfterReturning("yjh.devtoon.common.aop.PointcutConfig.presentationPackageConfig()")
    public void logMethodCall(final JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        log.info("Request: {} {} ({}), {}.class -> {}()", request.getMethod(), request.getRequestURI(), request.getRemoteHost(),
                className, methodName);
    }

}
