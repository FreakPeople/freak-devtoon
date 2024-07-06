package yjh.devtoon.webtoon.infrastructure;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageRepository {
    String upload(MultipartFile file);

    Resource get(String fileName);
}
