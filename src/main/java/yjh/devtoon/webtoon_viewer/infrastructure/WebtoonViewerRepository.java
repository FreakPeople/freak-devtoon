package yjh.devtoon.webtoon_viewer.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import yjh.devtoon.webtoon_viewer.domain.WebtoonViewerEntity;
import java.util.Optional;

public interface WebtoonViewerRepository extends JpaRepository<WebtoonViewerEntity, Long> {
    Optional<WebtoonViewerEntity> findByEmail(String email);
}