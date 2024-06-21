package yjh.devtoon.payment.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import yjh.devtoon.payment.domain.CookiePaymentEntity;
import java.util.Optional;

public interface CookiePaymentRepository extends JpaRepository<CookiePaymentEntity, Long> {

    Optional<CookiePaymentEntity> findByMemberId(Long memberId);

}
