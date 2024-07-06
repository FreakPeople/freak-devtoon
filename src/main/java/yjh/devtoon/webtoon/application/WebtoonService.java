package yjh.devtoon.webtoon.application;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yjh.devtoon.common.exception.DevtoonException;
import yjh.devtoon.common.exception.ErrorCode;
import yjh.devtoon.webtoon.constant.ErrorMessage;
import yjh.devtoon.webtoon.domain.Genre;
import yjh.devtoon.webtoon.domain.WebtoonEntity;
import yjh.devtoon.webtoon.dto.request.WebtoonCreateRequest;
import yjh.devtoon.webtoon.infrastructure.ImageRepository;
import yjh.devtoon.webtoon.infrastructure.WebtoonRepository;

@RequiredArgsConstructor
@Service
public class WebtoonService {

    private final WebtoonRepository webtoonRepository;
    private final ImageRepository imageRepository;

    public WebtoonEntity retrieve(final Long id) {
        return webtoonRepository.findById(id)
                .orElseThrow(() -> new DevtoonException(ErrorCode.NOT_FOUND,
                        ErrorMessage.getWebtoonNotFound(id)));
    }

    @Transactional
    public void createWebtoon(final WebtoonCreateRequest request, final MultipartFile devtoonImage) {
        validateTitleDuplicated(request.getTitle());

        try {
            // save image
            String imageUrl = imageRepository.upload(devtoonImage);

            // save webtoon
            WebtoonEntity webtoon = WebtoonEntity.create(
                    request.getTitle(),
                    request.getWriterName(),
                    imageUrl,
                    Genre.create(request.getGenre())
            );
            webtoonRepository.save(webtoon);
        } catch (Exception e) {
            // 만약 웹툰 저장 중에 실패하는 경우가 생기면, 이미지 또한 실패 처리해야한다.
            throw new IllegalArgumentException("[Error] create webtoon failure");
        }
    }

    private void validateTitleDuplicated(String title) {
        webtoonRepository.findByTitle(title)
                .ifPresent(webtoon -> {
                    throw new DevtoonException(ErrorCode.CONFLICT,
                            ErrorMessage.getTitleConflict(title));
                });
    }

    public Page<WebtoonEntity> retrieveAll(Pageable pageable) {
        return webtoonRepository.findAll(pageable);
    }

    public Resource retrieveImage(final Long id, final String fileName) {
        return imageRepository.get(fileName);
    }
}
