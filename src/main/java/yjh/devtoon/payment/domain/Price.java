package yjh.devtoon.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Price {

    @Column(name = "cookie_price", precision = 8, scale = 2)
    private BigDecimal amount;

    public static Price of(final BigDecimal amount) {
        return new Price(amount);
    }

    public static Price of(final int amount) {
        return new Price(BigDecimal.valueOf(amount));
    }

    private Price(final BigDecimal amount) {
        Objects.requireNonNull(amount);
        this.amount = toBigDecimal(amount);
    }

    private BigDecimal toBigDecimal(BigDecimal amount) {
        Objects.requireNonNull(amount);
        return new BigDecimal(amount.toString());
    }

    public Price plus(final Price price) {
        BigDecimal result = Calculator.PLUS.calculate(this.amount, price.getAmount());
        return new Price(result);
    }

    public Price minus(final Price price) {
        BigDecimal result = Calculator.MINUS.calculate(this.amount, price.getAmount());
        return new Price(result);
    }

    public Price multiply(final Price price) {
        BigDecimal result = Calculator.MULTIPLY.calculate(this.amount, price.getAmount());
        return new Price(result);
    }

    public Price divide(final Price price) {
        BigDecimal result = Calculator.DIVIDE.calculate(this.amount, price.getAmount());
        return new Price(result);
    }

    public Price calculateTotalPrice(Integer quantity) {
        BigDecimal quantityAsBigDecimal = BigDecimal.valueOf(quantity);
        BigDecimal result = quantityAsBigDecimal.multiply(this.amount);
        return new Price(result);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(amount, price.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }

    @Override
    public String toString() {
        return "Price{" +
                "amount=" + amount +
                '}';
    }

}
