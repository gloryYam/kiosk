package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sample.cafekiosk.spring.api.service.file.FileStore;
import sample.cafekiosk.spring.domain.product.Image;
import sample.cafekiosk.spring.domain.product.Product;

import java.io.IOException;

import static sample.cafekiosk.spring.domain.product.ImageType.MAIN;


@Service
@RequiredArgsConstructor
public class ImageService {

    private final FileStore fileStore;

    public void addImageToProduct(MultipartFile uploadImageFile, Product product) throws IOException {
        Image image = uploadMainImage(uploadImageFile);
        product.addImage(image);
    }

    public void updateImageToProduct(Product product, MultipartFile updateImageFile) throws IOException {
        Image image = uploadMainImage(updateImageFile);
        product.addImage(image);
    }

    private Image uploadMainImage(MultipartFile uploadImageFile) throws IOException {
        return fileStore.uploadImageFile(uploadImageFile, MAIN);
    }
}
