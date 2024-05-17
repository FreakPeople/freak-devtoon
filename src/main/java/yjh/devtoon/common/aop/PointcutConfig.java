package yjh.devtoon.common.aop;

import org.aspectj.lang.annotation.Pointcut;

public class PointcutConfig {

    /**
     * presentation 계층의 모든 메서드에 적용하는 포인트컷 경로.
     */
    @Pointcut("execution(* yjh.devtoon.*.presentation.*.*(..))")
    public void presentationPackageConfig() {
    }

}
