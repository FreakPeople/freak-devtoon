package yjh.devtoon.policy.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yjh.devtoon.policy.domain.CookiePolicyEntity;
import java.math.BigDecimal;
import java.util.Optional;

public interface CookiePolicyRepository extends JpaRepository<CookiePolicyEntity, Long> {

    /**
     * 현재 적용되는 cookie 가격을 조회합니다.
     */
    @Query("SELECT c.cookiePrice FROM CookiePolicyEntity c WHERE c.endDate is null")
    BigDecimal findActiveCookiePrice();

    /**
     * 현재 적용되는 cookieQuantityPerEpisode 조회합니다.
     */
    @Query("SELECT c.cookieQuantityPerEpisode FROM CookiePolicyEntity c WHERE c.endDate is null")
    Integer findActiveCookieQuantityPerEpisode();

    /**
     * 현재 적용되는 CookiePolicy 조회합니다.
     */
    @Query("SELECT c FROM CookiePolicyEntity c WHERE c.endDate is null")
    Optional<CookiePolicyEntity> findActiveCookiePolicy();

}