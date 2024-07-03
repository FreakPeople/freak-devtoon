package yjh.devtoon.webtoon.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;

@Repository
public class LocalImageRepository implements ImageRepository {

    @Value("${file.local.upload.path}")
    private String uploadPath;

    @Override
    public String upload(final MultipartFile file) {
        final String uuid = UUID.randomUUID().toString();
        final String originalName = file.getOriginalFilename();
        final Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);

        try {
            // 디렉토리가 존재하지 않으면 생성
            Files.createDirectories(savePath.getParent());

            // 디렉토리에 저장
            file.transferTo(savePath);
        } catch (Exception e) {
            throw new IllegalArgumentException("[Error] image upload failure");
        } finally {
            return savePath.getFileName().toString();
        }
    }

    public void deleteDirectory(Path path) throws IOException {
        if (!Files.exists(path)) {
            return;
        }

        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
