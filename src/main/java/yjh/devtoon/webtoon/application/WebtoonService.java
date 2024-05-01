package yjh.devtoon.webtoon.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.webtoon.domain.WebtoonEntity;
import yjh.devtoon.webtoon.dto.request.WebtoonCreateRequest;
import yjh.devtoon.webtoon.infrastructure.WebtoonRepository;

@RequiredArgsConstructor
@Service
public class WebtoonService {
    private final WebtoonRepository webtoonRepository;

    @Transactional
    public void createWebtoon(final WebtoonCreateRequest request) {
        validateTitleDuplicated(request.getTitle());

        WebtoonEntity webtoon = WebtoonEntity.create(request);

        webtoonRepository.save(webtoon);
    }

    private void validateTitleDuplicated(String title) {
        webtoonRepository.findByTitle(title)
                .ifPresent(webtoon -> {
                    throw new IllegalArgumentException("해당 이름의 웹툰이 존재 합니다.");
                });
    }

}
