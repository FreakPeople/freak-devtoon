package yjh.devtoon.payment.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BiFunction;

public enum Calculator {
    PLUS("더하기", BigDecimal::add),
    MINUS("빼기", BigDecimal::subtract),
    MULTIPLY("곱하기", BigDecimal::multiply),
    DIVIDE("나누기", (a, b) -> a.divide(b, 3, RoundingMode.HALF_UP));

    private final String name;
    private final BiFunction<BigDecimal, BigDecimal, BigDecimal> biFunction;

    Calculator(String name, BiFunction<BigDecimal, BigDecimal, BigDecimal> biFunction) {
        this.name = name;
        this.biFunction = biFunction;
    }

    public BigDecimal calculate(BigDecimal a, BigDecimal b) {
        return this.biFunction.apply(a, b);
    }

}
