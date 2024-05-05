package yjh.devtoon.promotion.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import yjh.devtoon.promotion.domain.PromotionAttributeEntity;
import java.util.Optional;

public interface PromotionAttributeRepository extends JpaRepository<PromotionAttributeEntity, Long> {

    Optional<PromotionAttributeEntity> findByPromotionEntityId(Long promotionId);

}
