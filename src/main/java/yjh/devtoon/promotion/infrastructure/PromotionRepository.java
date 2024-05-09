package yjh.devtoon.promotion.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yjh.devtoon.promotion.domain.PromotionEntity;
import java.time.LocalDateTime;
import java.util.List;

public interface PromotionRepository extends JpaRepository<PromotionEntity, Long> {

    /**
     * 요청된 기간 내 유효한 프로모션이 데이터베이스에 존재하는지 확인합니다.
     */
    @Query("SELECT COUNT(p) > 0 FROM PromotionEntity p WHERE p.startDate <= :endDate AND p.endDate >= :startDate")
    boolean existsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * 요청된 기간 내 모든 유효한 프로모션을 페이지화하여 반환합니다.
     */
    @Query("SELECT p FROM PromotionEntity p WHERE p.startDate <= :endDate AND p.endDate >= :startDate")
    Page<PromotionEntity> findActivePromotions(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    /**
     * 현재 적용 가능한 모든 유효한 프로모션을 리스트로 반환합니다.
     * : 현재 시간이 프로모션 적용시간 시작과 끝 사이여야한다.
     */
    @Query("SELECT p FROM PromotionEntity p WHERE p.startDate <= :now AND p.endDate >= :now")
    List<PromotionEntity> activePromotions(@Param("now") LocalDateTime now);


}
