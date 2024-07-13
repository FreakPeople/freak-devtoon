package yjh.devtoon.webtoon.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yjh.devtoon.webtoon.domain.WebtoonEntity;
import java.util.Optional;

public interface WebtoonRepository extends JpaRepository<WebtoonEntity, Long> {
    Optional<WebtoonEntity> findByTitle(String title);

    Page<WebtoonEntity> findAllByWriterName(Pageable pageable, String name);
}
