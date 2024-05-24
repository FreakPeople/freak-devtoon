package yjh.devtoon.promotion.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import yjh.devtoon.promotion.domain.PromotionAttributeEntity;
import java.util.List;

public interface PromotionAttributeRepository extends JpaRepository<PromotionAttributeEntity,
        Long> {

    /**
     * 현재 적용 가능한 프로모션에 포함된 모든 프로모션 속성 조회
     */
    List<PromotionAttributeEntity> findByPromotionEntityId(Long promotionId);

}
