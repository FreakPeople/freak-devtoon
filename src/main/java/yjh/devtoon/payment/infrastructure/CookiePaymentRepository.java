package yjh.devtoon.payment.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import yjh.devtoon.payment.domain.CookiePaymentEntity;

public interface CookiePaymentRepository extends JpaRepository<CookiePaymentEntity, Long> {
}
