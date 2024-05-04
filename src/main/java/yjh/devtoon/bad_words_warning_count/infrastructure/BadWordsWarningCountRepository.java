package yjh.devtoon.bad_words_warning_count.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import yjh.devtoon.bad_words_warning_count.domain.BadWordsWarningCountEntity;

public interface BadWordsWarningCountRepository extends JpaRepository<BadWordsWarningCountEntity, Long> {
}