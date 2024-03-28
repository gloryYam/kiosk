package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.product.repository.ProductRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

@ActiveProfiles("test")
@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("원하는 판매상태를 가진 상품들을 조회")
    void findAllBySellingStatusIn() {
        // given
        Product product1 = createProduct("001",HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002",HANDMADE, HOLD, "카페라떼", 5000);
        Product product3 = createProduct("003",HANDMADE, STOP_SELLING, "팥빙수", 8000);

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        // then

        /**
         *  리스트를 검증할 때 처음에는 사이즈 체크 그리고
         *  extracting, contains 를 주로 사용
         */
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")  // 검증하고자 하는 필드 추출 가능
                .containsExactlyInAnyOrder(     // 순서 상관없이 (반대 -> containsExactly())
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

    @Test
    @DisplayName("상품번호 리스트로 상품들을 조회한다.")
    void findAllByProductNumberIn() {
        // given
        Product product1 = createProduct("001",HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002",HANDMADE, HOLD, "카페라떼", 5000);
        Product product3 = createProduct("003",HANDMADE, STOP_SELLING, "팥빙수", 8000);

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        // then

        /**
         *  리스트를 검증할 때 처음에는 사이즈 체크 그리고
         *  extracting, contains 를 주로 사용
         */
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")  // 검증하고자 하는 필드 추출 가능
                .containsExactlyInAnyOrder(     // 순서 상관없이 (반대 -> containsExactly())
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

    @Test
    @DisplayName("가장 마지막으로 저장한 상품 번호를 읽어온다.")
    void findLatestProductNumber() {
        // given
        Product product1 = createProduct("001",HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002",HANDMADE, HOLD, "카페라떼", 5000);
        Product product3 = createProduct("003",HANDMADE, STOP_SELLING, "팥빙수", 8000);


        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        String latestProductNumber = productRepository.findLatestProductNumber();

        // then
        assertThat(latestProductNumber).isEqualTo("003");
    }

    @Test
    @DisplayName("가장 마지막으로 저장한 상품 번호를 읽어올 때, 상품이 하나도 없는 경우에는 null 을 반환한다..")
    void findLatestProductNumberWithProductIsEmpty() {
        // given
        // when
        String latestProductNumber = productRepository.findLatestProductNumber();

        // then
        assertThat(latestProductNumber).isNull();
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