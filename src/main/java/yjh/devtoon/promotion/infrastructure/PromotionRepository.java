package yjh.devtoon.promotion.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yjh.devtoon.promotion.domain.PromotionEntity;
import java.time.LocalDateTime;
import java.util.List;

public interface PromotionRepository extends JpaRepository<PromotionEntity, Long> {

    /**
     * 현재와 미래 프로모션을 리스트로 반환합니다.
     */
    @Query("SELECT p FROM PromotionEntity p WHERE  p.endDate IS NULL OR p.endDate >= :now")
    List<PromotionEntity> findCurrentOrFuturePromotions(LocalDateTime now);

    /**
     * 종료된 모든 프로모션 조회
     */
    @Query("SELECT p FROM PromotionEntity p WHERE p.deletedAt IS NOT NULL")
    List<PromotionEntity> findEndedPromotions();

}
