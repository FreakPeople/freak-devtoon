package yjh.devtoon.policy.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import yjh.devtoon.policy.domain.BadWordsPolicyEntity;

public interface BadWordsPolicyRepository extends JpaRepository<BadWordsPolicyEntity, Long> {
}
