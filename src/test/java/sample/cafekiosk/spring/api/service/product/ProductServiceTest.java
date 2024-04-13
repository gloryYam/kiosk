package sample.cafekiosk.spring.api.service.product;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.request.ProductUpdateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.*;
import sample.cafekiosk.spring.domain.product.repository.ProductRepository;
import sample.cafekiosk.spring.exception.product.ProductNotFound404Exception;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@SpringBootTest
class ProductServiceTest extends IntegrationTestSupport {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageService imageService;


    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("신규 상품을 등록")
    void createProduct() throws IOException {
        // given
        MockMultipartFile mockFile = getMockMultipartFile();

        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, SELLING, "스무디", 5000);
        productRepository.saveAll(List.of(product1, product2));

        ProductCreateRequest request = ProductCreateRequest.builder()
            .type(HANDMADE)
            .sellingStatus(SELLING)
            .name("아이스티")
            .price(3000)
            .build();
        // when
        ProductResponse productResponse = productService.createProduct(request.toServiceRequest(), mockFile);

        // then
        assertThat(productResponse)
            .extracting("productNumber", "type", "sellingStatus", "name", "price")
            .contains("003", HANDMADE, SELLING, "아이스티", 3000);

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(3)
            .extracting("productNumber", "type", "sellingStatus", "name", "price")
            .containsExactlyInAnyOrder(
                tuple("001", HANDMADE, SELLING, "아메리카노", 4000),
                tuple("002", HANDMADE, SELLING, "스무디", 5000),
                tuple("003", HANDMADE, SELLING, "아이스티", 3000)
            );

        assertThat(productResponse.getUploadFileImage()).isNotNull();
        assertThat(productResponse.getUploadFileImage().getOriginFileName()).isEqualTo("test.jpg");
        assertThat(productResponse.getUploadFileImage().getStoreFileName()).isNotEmpty();
    }

    @Test
    @DisplayName("상품이 하나도 없는 경우 상품을 등록하면 상품번호는 001이다.")
    void createProductWhenProductIsEmpty() throws IOException {
        // given
        MockMultipartFile mockFile = getMockMultipartFile();

        ProductCreateRequest request = ProductCreateRequest.builder()
            .type(HANDMADE)
            .sellingStatus(SELLING)
            .name("스무디")
            .price(5000)
            .build();
        // when
        ProductResponse productResponse = productService.createProduct(request.toServiceRequest(), mockFile);

        // then
        assertThat(productResponse)
            .extracting("productNumber", "type", "sellingStatus", "name", "price")
            .contains("001", HANDMADE, SELLING, "스무디", 5000);

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1)
            .extracting("productNumber", "type", "sellingStatus", "name", "price")
            .contains(
                tuple("001", HANDMADE, SELLING, "스무디", 5000)
            );

    }

    @Test
    @DisplayName("기존 상품을 수정한다.")
    void productUpdate() throws IOException {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아이스티", 3000);
        Product product2 = createProduct("002", HANDMADE, SELLING, "카페라떼", 4000);
        List<Product> products = productRepository.saveAll(List.of(product1, product2));

        MockMultipartFile mockFile = getMockMultipartFile();

        ProductUpdateServiceRequest request = ProductUpdateServiceRequest.builder()
            .type(HANDMADE)
            .sellingStatus(STOP_SELLING)
            .name("복숭아 아이스티")
            .price(3000)
            .build();

        Product first = products.stream()
            .filter(p -> p.getProductNumber().equals("002"))
            .findAny().orElseThrow();

        // when
        ProductResponse updateProduct = productService.updateProduct(first.getId(), request, mockFile);

        // then
        assertThat(updateProduct)
            .extracting("sellingStatus", "name")
            .containsExactly(
                STOP_SELLING, "복숭아 아이스티"
            );

        assertThat(updateProduct.getUploadFileImage().getOriginFileName()).isEqualTo(mockFile.getOriginalFilename());

    }

    @Test
    @DisplayName("ProductNotFoundException 이 잘 터지는 확인")
    void productNotFoundException() throws IOException {
        // given
        Product product = createProduct("001", HANDMADE, SELLING, "아이스티", 3000);
        Product savedProduct = productRepository.save(product);

        MockMultipartFile mockFile = getMockMultipartFile();

        ProductUpdateServiceRequest request = ProductUpdateServiceRequest.builder()
            .type(HANDMADE)
            .sellingStatus(STOP_SELLING)
            .name("복숭아 아이스티")
            .price(3000)
            .build();

        // when
        assertThatThrownBy(() -> productService.updateProduct(4L, request, mockFile))
            .isInstanceOf(ProductNotFound404Exception.class)
            .hasMessage("상품을 찾을 수 없습니다");

    }

    @Test
    @DisplayName("등록된 상품을 삭제한다.")
    void deleteProduct() {
        // given
        Product product = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        productRepository.save(product);

        // when
        productService.deleteProduct(product.getId());

        // then
        assertThat(productRepository.count()).isEqualTo(0);
    }

    private Product createProduct(String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        return Product.builder()
            .productNumber(productNumber)
            .type(type)
            .sellingStatus(sellingStatus)
            .name(name)
            .price(price)
            .build();
    }

    private static MockMultipartFile getMockMultipartFile() throws IOException {
        byte[] imageContent = Files.readAllBytes(Paths.get("src/test/resources/test.jpg"));

        return new MockMultipartFile("mainImage", "test.jpg", "image/jpg", imageContent);
    }
}