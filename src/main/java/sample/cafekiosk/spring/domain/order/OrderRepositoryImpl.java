package sample.cafekiosk.spring.domain.order;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static sample.cafekiosk.spring.domain.order.QOrder.order;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Order> findOrderBy(LocalDateTime startDateTime, LocalDateTime endDateTime, OrderStatus orderStatus) {
        return queryFactory
            .selectFrom(order)
            .where(order.registeredDateTime.goe(startDateTime),
                order.registeredDateTime.lt(endDateTime),
                order.orderStatus.eq(orderStatus))
            .fetch();

    }
}
