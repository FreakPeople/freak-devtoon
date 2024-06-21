package yjh.devtoon.member.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import yjh.devtoon.member.domain.MemberEntity;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByEmail(String email);
}