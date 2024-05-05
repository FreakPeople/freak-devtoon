package yjh.devtoon.comment.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import yjh.devtoon.comment.domain.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}