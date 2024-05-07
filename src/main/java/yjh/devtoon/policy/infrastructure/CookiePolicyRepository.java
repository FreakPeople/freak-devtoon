package yjh.devtoon.policy.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import yjh.devtoon.policy.domain.CookiePolicyEntity;

public interface CookiePolicyRepository extends JpaRepository<CookiePolicyEntity, Long> {
}
