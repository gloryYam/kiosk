package sample.cafekiosk.spring.api.service.file;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.domain.product.Image;
import sample.cafekiosk.spring.domain.product.ImageType;
import sample.cafekiosk.spring.exception.file.FileEmptyException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class FileStoreTest extends IntegrationTestSupport {

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private FileStore fileStore;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        System.setProperty("users.dudrh", tempDir.toString());
    }

    @AfterEach
    void tearDown() {
        System.clearProperty("users.dudrh");
    }

    @Test
    @DisplayName("파일 업로드 테스트")
    void FileStoreTest() throws IOException {

        System.out.println("Temp directory: " + tempDir);
        // given
        String originalFilename = "test.jpg";
        byte[] imageContent = Files.readAllBytes(Paths.get("src/test/resources/test.jpg"));

        MockMultipartFile file = new MockMultipartFile(
            "file", originalFilename, "image/jpg", imageContent);

        ImageType main = ImageType.MAIN;

        // when
        Image image = fileStore.uploadImageFile(file, main);

        // then
        assertThat(image.getOriginFileName()).isEqualTo(originalFilename);
        assertThat(image.getStoreFileName()).isNotNull();
        assertThat(image.getStoreFileName()).isNotEmpty();
        assertThat(image.getImageType()).isEqualTo(main);
        assertThat(Files.exists(Paths.get(tempDir.toString(), image.getStoreFileName()))).isTrue();
    }

    @Test
    @DisplayName("파일 업로드 실패 테스트: 파일이 없을 때")
    void failedFileUploadEmptyFile() throws IOException {

        // given
        String originalFilename = "test.jpg";

        MockMultipartFile file = new MockMultipartFile("file", originalFilename, "image/jpg", new byte[0]);

        ImageType main = ImageType.MAIN;

        // when // then
        assertThatThrownBy(() -> fileStore.uploadImageFile(file, main))
            .isInstanceOf(FileEmptyException.class)
            .hasMessage("파일 비어있습니다.");
    }

}



















