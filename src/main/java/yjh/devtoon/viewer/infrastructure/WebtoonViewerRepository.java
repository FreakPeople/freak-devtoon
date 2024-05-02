package yjh.devtoon.viewer.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import yjh.devtoon.viewer.domain.WebtoonViewerEntity;
import java.util.Optional;

public interface WebtoonViewerRepository extends JpaRepository<WebtoonViewerEntity, Long> {
    Optional<WebtoonViewerEntity> findByEmail(String email);
}