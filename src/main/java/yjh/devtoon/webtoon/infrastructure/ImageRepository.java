package yjh.devtoon.webtoon.infrastructure;

import org.springframework.web.multipart.MultipartFile;

public interface ImageRepository {
    String upload(MultipartFile file);
}
