package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.repository.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@SpringBootTest
class OrderRepositoryCustomTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("날짜로 주문 조회하기")
    void findByOrderDateBetween() {
        // given
        List<Product> products = List.of(
            createProduct("001", HANDMADE, SELLING, "아메리카노", 4000),
            createProduct("002", HANDMADE, SELLING, "라떼", 5000),
            createProduct("003", HANDMADE, SELLING, "스무디", 6000)
        );
        productRepository.saveAll(products);

        Order orders = Order.create(products, LocalDateTime.of(2024, 2, 9, 10, 20));
        orderRepository.save(orders);

        PageRequest pageable = PageRequest.of(0, 10);
        // when
        Page<Order> result = orderRepository.findByOrderDateBetween(
            LocalDate.of(2024, 2, 1),
            LocalDate.of(2024, 2, 10),
            pageable
        );

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent())
            .extracting("id")
            .contains(orders.getId());


    }

    @Test
    @DisplayName("날짜로 주문을 했지만 결과가 없는 경우")
    void orderByDate() {
        // given
        List<Product> products = List.of(
            createProduct("001", HANDMADE, SELLING, "아메리카노", 4000),
            createProduct("002", HANDMADE, SELLING, "라떼", 5000),
            createProduct("003", HANDMADE, SELLING, "스무디", 6000)
        );
        productRepository.saveAll(products);

        Order orders = Order.create(products, LocalDateTime.of(2024, 2, 9, 10, 20));
        orderRepository.save(orders);

        PageRequest pageable = PageRequest.of(0, 10);
        // when
        Page<Order> result = orderRepository.findByOrderDateBetween(
            LocalDate.of(2024, 2, 10),
            LocalDate.of(2024, 2, 10),
            pageable
        );

        // then
        assertThat(result.getContent()).hasSize(0);
    }


    private Product createProduct(String productNumber, ProductType type, ProductSellingStatus
        sellingStatus, String name, int price) {
        return Product.builder()
            .productNumber(productNumber)
            .type(type)
            .sellingStatus(sellingStatus)
            .name(name)
            .price(price)
            .build();
    }
}