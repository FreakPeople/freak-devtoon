package yjh.devtoon.comment.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yjh.devtoon.comment.domain.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findByWebtoonId(Long webtoonId, Pageable pageable);
}