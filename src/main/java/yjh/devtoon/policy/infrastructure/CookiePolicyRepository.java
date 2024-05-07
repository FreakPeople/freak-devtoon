package yjh.devtoon.policy.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yjh.devtoon.policy.domain.CookiePolicyEntity;
import java.time.LocalDateTime;
import java.util.List;

public interface CookiePolicyRepository extends JpaRepository<CookiePolicyEntity, Long> {

    @Query("SELECT c FROM CookiePolicyEntity c WHERE c.startDate <= :now AND (c.endDate IS NULL OR c.endDate >= :now)")
    List<CookiePolicyEntity> findActivePolicies(@Param("now") LocalDateTime now);

}
