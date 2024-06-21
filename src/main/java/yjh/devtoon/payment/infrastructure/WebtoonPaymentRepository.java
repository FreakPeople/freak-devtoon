package yjh.devtoon.payment.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import yjh.devtoon.payment.domain.WebtoonPaymentEntity;
import java.util.Optional;

public interface WebtoonPaymentRepository extends JpaRepository<WebtoonPaymentEntity, Long> {

    Optional<WebtoonPaymentEntity> findByMemberId(Long memberId);
}
