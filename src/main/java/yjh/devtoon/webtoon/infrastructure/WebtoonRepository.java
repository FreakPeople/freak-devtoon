package yjh.devtoon.webtoon.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import yjh.devtoon.webtoon.domain.WebtoonEntity;

public interface WebtoonRepository extends JpaRepository<WebtoonEntity, Long> {
}
