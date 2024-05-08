package yjh.devtoon.policy.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yjh.devtoon.policy.domain.BadWordsPolicyEntity;
import java.time.LocalDateTime;
import java.util.List;

public interface BadWordsPolicyRepository extends JpaRepository<BadWordsPolicyEntity, Long> {

    @Query("SELECT b FROM BadWordsPolicyEntity b WHERE b.startDate <= :now AND (b.endDate IS NULL OR b.endDate >= :now)")
    List<BadWordsPolicyEntity> findActivePolicies(@Param("now") LocalDateTime now);

}
