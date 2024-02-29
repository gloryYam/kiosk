package sample.cafekiosk.spring.domain.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OrderBy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static sample.cafekiosk.spring.domain.product.QProduct.*;

public class ProductRepositoryImpl implements ProductRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public String findLatestProductNumber() {
        return queryFactory
            .select(product.productNumber)
            .from(product)
            .orderBy(product.id.desc())
            .limit(1)
            .fetchOne();
    }
}
