package yjh.devtoon.webtoon.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
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
                    throw new DevtoonException(ErrorCode.CONFLICT, "title : '%s' 가 존재합니다.".formatted(title));
                });
    }

}
