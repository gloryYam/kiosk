package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.request.ProductUpdateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.repository.ProductRepository;
import sample.cafekiosk.spring.exception.product.ProductNotFound404Exception;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService{

    private final ProductRepository productRepository;
    private final ImageService imageService;

    /**
     * 등록
     */
    @Transactional
    public ProductResponse createProduct(ProductCreateServiceRequest request, MultipartFile mainImage) throws IOException {
        String nextProductNumber = createNextProductNumber();
        Product product = request.toEntity(nextProductNumber);

        imageService.addImageToProduct(mainImage, product);
        Product saveProduct = productRepository.save(product);
        return ProductResponse.of(saveProduct);
    }

    /**
     * 수정
     */
    @Transactional
    public ProductResponse updateProduct(Long id, ProductUpdateServiceRequest request, MultipartFile updateImage) throws IOException {
        // 상품을 가져온다.
        Product product = productRepository.findById(id)
            .orElseThrow(ProductNotFound404Exception::new);

        imageService.updateImageToProduct(product, updateImage);
        Product newProduct = request.toEntity(product.getProductNumber());

        product.updateProduct(newProduct);
        return ProductResponse.of(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(ProductNotFound404Exception::new);

        productRepository.delete(product);
    }

    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();
        if(latestProductNumber == null) {
            return "001";
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;

        return String.format("%03d", nextProductNumberInt);
    }

}
