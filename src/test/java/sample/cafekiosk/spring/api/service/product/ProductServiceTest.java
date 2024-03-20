package sample.cafekiosk.spring.api.service.product;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.request.ProductUpdateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.exception.product.ProductNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest extends IntegrationTestSupport {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("신규 상품을 등록")
    void createProduct() {
        // given
        Product product = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        productRepository.save(product);

        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("스무디")
                .price(5000)
                .build();
        // when
        ProductResponse productResponse = productService.createProduct(request.toServiceRequest());

        // then
            assertThat(productResponse)
                    .extracting("productNumber", "type", "sellingStatus", "name", "price")
                    .contains("002", HANDMADE, SELLING, "스무디", 5000);

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(2)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", HANDMADE, SELLING, "아메리카노", 4000),
                        tuple("002", HANDMADE, SELLING, "스무디", 5000)
                );

    }

    @Test
    @DisplayName("상품이 하나도 없는 경우 상품을 등록하면 상품번호는 001이다.")
    void createProductWhenProductIsEmpty() {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("스무디")
                .price(5000)
                .build();
        // when
        ProductResponse productResponse = productService.createProduct(request.toServiceRequest());

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
    void productUpdate() {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아이스티", 3000);
        Product product2 = createProduct("002", HANDMADE, SELLING, "카페라떼", 4000);
        List<Product> products = productRepository.saveAll(List.of(product1, product2));


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
        ProductResponse updateProduct = productService.updateProduct(first.getId(), request);

        // then
        assertThat(updateProduct)
            .extracting("sellingStatus", "name")
            .containsExactly(
                STOP_SELLING, "복숭아 아이스티"
            );

    }

    @Test
    @DisplayName("ProductNotFoundException 이 잘 터지는 확인")
    void productNotFoundException() {
        // given
        Product product = createProduct("001", HANDMADE, SELLING, "아이스티", 3000);
        Product savedProduct = productRepository.save(product);


        ProductUpdateServiceRequest request = ProductUpdateServiceRequest.builder()
            .type(HANDMADE)
            .sellingStatus(STOP_SELLING)
            .name("복숭아 아이스티")
            .price(3000)
            .build();

        // when
        assertThatThrownBy(() -> productService.updateProduct(4L, request))
            .isInstanceOf(ProductNotFoundException.class)
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

}