package sample.cafekiosk.spring.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@SpringBootTest
class StockRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;


    @Test
    @DisplayName("상품번호 리스트로 재고를 조회한다.")
    void findAllByProductNumberIn() {
        // given
        Stock stock1 = Stock.create("001", 1);
        Stock stock2 = Stock.create("002", 2);
        Stock stock3 = Stock.create("003", 3);

        stockRepository.saveAll(List.of(stock1, stock2, stock3));

        // when
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(List.of("001", "002"));

        // then

        /**
         *  리스트를 검증할 때 처음에는 사이즈 체크 그리고
         *  extracting, contains 를 주로 사용
         */
        assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")  // 검증하고자 하는 필드 추출 가능
                .containsExactlyInAnyOrder(     // 순서 상관없이 (반대 -> containsExactly())
                        tuple("001", 1),
                        tuple("002", 2)
                );
    }

}