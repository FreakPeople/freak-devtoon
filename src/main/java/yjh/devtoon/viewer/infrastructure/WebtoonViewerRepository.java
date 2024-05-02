package yjh.devtoon.viewer.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import yjh.devtoon.viewer.domain.WebtoonViewerEntity;

public interface WebtoonViewerRepository extends JpaRepository<WebtoonViewerEntity, Long> {
}