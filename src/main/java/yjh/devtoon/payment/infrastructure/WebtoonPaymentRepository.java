package yjh.devtoon.payment.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import yjh.devtoon.payment.domain.WebtoonPaymentEntity;

public interface WebtoonPaymentRepository extends JpaRepository<WebtoonPaymentEntity, Long> {
}
