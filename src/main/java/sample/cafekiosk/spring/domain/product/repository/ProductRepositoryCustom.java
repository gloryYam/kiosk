package sample.cafekiosk.spring.domain.product.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

public interface ProductRepositoryCustom {

    String findLatestProductNumber();
}
