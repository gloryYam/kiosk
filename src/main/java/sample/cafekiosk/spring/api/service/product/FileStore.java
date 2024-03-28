package sample.cafekiosk.spring.api.service.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sample.cafekiosk.spring.domain.product.Image;
import sample.cafekiosk.spring.domain.product.ImageType;
import sample.cafekiosk.spring.exception.file.EmptyFileException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Component
public class FileStore {

    public String getFilePath() {
        Path filePath = Paths.get(System.getProperty("users.dudrh"));

        try {
            if(Files.notExists(filePath)) {
                Files.createDirectories(filePath);
            }
        } catch (IOException e) {
            log.error("Failed to create directory", filePath, e);

        } catch (SecurityException e){
            log.error("Permission denied to create directory: " + filePath, e);
        }

        return filePath + File.separator;
    }


    public String getFullPath(String fileName) {
        return getFilePath() + fileName;
    }


    public Image uploadImageFile(MultipartFile multipartFile, ImageType type) throws IOException {
        if(multipartFile.isEmpty()) {
            throw new EmptyFileException();
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        File file = new File(getFullPath(storeFileName));

        try {

            multipartFile.transferTo(file);
            return Image.of(originalFilename, storeFileName, type);

        } catch(IOException e) {                                                // todo 관련 exception 만들기
            log.error("failed to upload file", e);
            throw new IOException("Failed to upload file", e);                  // todo 관련 exception 으로 리턴하기
        }
    }

    private static String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private static String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
