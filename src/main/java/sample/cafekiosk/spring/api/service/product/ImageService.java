package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.service.file.FileStore;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.domain.product.Image;
import sample.cafekiosk.spring.domain.product.Product;

import java.io.IOException;

import static sample.cafekiosk.spring.domain.product.ImageType.MAIN;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final FileStore fileStore;

    public void addImageToProduct(ProductCreateServiceRequest request, Product product) throws IOException {
        Image image = uploadMainImage(request);
        product.addImage(image);
    }

    public Image uploadMainImage(ProductCreateServiceRequest request) throws IOException {
        return fileStore.uploadImageFile(request.getMainImage(), MAIN);
    }
}
