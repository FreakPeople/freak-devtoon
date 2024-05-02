package yjh.devtoon.promotion.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import yjh.devtoon.promotion.domain.PromotionAttributeEntity;

public interface PromotionAttributeRepository extends JpaRepository<PromotionAttributeEntity, Long> {
}
