package yjh.devtoon.promotion.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import yjh.devtoon.promotion.domain.PromotionEntity;

public interface PromotionRepository extends JpaRepository<PromotionEntity, Long> {
}
