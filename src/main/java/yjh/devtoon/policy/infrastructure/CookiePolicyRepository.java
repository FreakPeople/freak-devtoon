package yjh.devtoon.policy.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yjh.devtoon.policy.domain.CookiePolicyEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CookiePolicyRepository extends JpaRepository<CookiePolicyEntity, Long> {

    /**
     * 현재 적용되는 cookie 가격을 조회합니다.
     * : 현재 시간이 cookie 정책의 시작과 끝 시간 사이여야한다.
     */
    @Query("SELECT c.cookiePrice FROM CookiePolicyEntity c WHERE c.startDate <= :now AND c" +
            ".endDate >= :now")
    BigDecimal activeCookiePrice(@Param("now") LocalDateTime now);

    /**
     * 현재 적용되는 cookieQuantityPerEpisode 조회합니다.
     */
    @Query("SELECT c.cookieQuantityPerEpisode FROM CookiePolicyEntity c WHERE c.endDate is null")
    Integer findActiveCookieQuantityPerEpisode();

}