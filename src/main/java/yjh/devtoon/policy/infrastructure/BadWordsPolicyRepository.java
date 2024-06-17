package yjh.devtoon.policy.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yjh.devtoon.policy.domain.BadWordsPolicyEntity;
import java.util.Optional;

public interface BadWordsPolicyRepository extends JpaRepository<BadWordsPolicyEntity, Long> {

    /**
     * 현재 적용되는 BadWordsPolicy 조회합니다.
     */
    @Query("SELECT b FROM BadWordsPolicyEntity b WHERE b.endDate is null")
    Optional<BadWordsPolicyEntity> findActiveBadWordsPolicy();

}
