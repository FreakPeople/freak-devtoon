package yjh.devtoon.payment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

@DisplayName("도메인 단위 테스트 [Price]")
class PriceTest {

    @DisplayName("[of() 테스트] : Price 생성 테스트 성공")
    @Test
    void createPrice_successfully() {
        // given
        int num = 5;

        // when
        Price price = Price.of(num);

        // then
        assertThat(price).isEqualTo(Price.of(5));
    }

    @DisplayName("[of() 테스트] : Price 생성 테스트 실패")
    @Test
    void givenNullAmount_whenCreatePrice_thenThrowException() {
        assertThatThrownBy(() -> Price.of(null))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("[plus, minus, multiply, divide 테스트] : 계산 성공 테스트")
    @Test
    void calculate_successfully() {
        // given
        Price num_1 = Price.of(BigDecimal.valueOf(8));
        Price num_2 = Price.of(BigDecimal.valueOf(4));

        // when, then
        assertAll(
                () -> assertThat(num_1.plus(num_2).getAmount()).isEqualTo(Price.of(BigDecimal.valueOf(12)).getAmount()),
                () -> assertThat(num_1.minus(num_2).getAmount()).isEqualTo(Price.of(BigDecimal.valueOf(4)).getAmount()),
                () -> assertThat(num_1.multiply(num_2).getAmount()).isEqualTo(Price.of(BigDecimal.valueOf(32)).getAmount()),
                () -> assertThat(num_1.divide(num_2).getAmount()).isEqualTo(Price.of(new BigDecimal("2.000")).getAmount())
        );
    }

    @DisplayName("[plus, minus, multiply, divide 테스트] : 계산 실패 테스트")
    @Test
    void givenNumbers_whenCalculated_thenThrowException() {
        // given
        Price num_1 = Price.of(8);
        Price num_2 = Price.of(4);

        // when, then
        assertAll(
                () -> assertThat(num_1.plus(num_2)).isNotEqualTo(Price.of(999)),
                () -> assertThat(num_1.minus(num_2)).isNotEqualTo(Price.of(999)),
                () -> assertThat(num_1.multiply(num_2)).isNotEqualTo(Price.of(999)),
                () -> assertThat(num_1.divide(num_2)).isNotEqualTo(Price.of(new BigDecimal("999")))
        );
    }

    @DisplayName("[calculateTotalPrice() 테스트] : TotalPrice 계산 테스트")
    @Test
    void calculateTotalPrice_successfully() {
        // given
        int quantity = 5;
        Price price = Price.of(10000);

        // when
        Price totalPrice = price.calculateTotalPrice(quantity);

        // then
        assertThat(totalPrice).isEqualTo(Price.of(50000));
    }

}