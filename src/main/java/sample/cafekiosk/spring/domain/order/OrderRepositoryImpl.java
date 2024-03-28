package sample.cafekiosk.spring.domain.order;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @Override
    public Page<Order> findByOrderDateBetween(LocalDate startDateTime, LocalDate endDateTime, Pageable pageable) {

        List<Order> content = fetchOrdersBetweenDates(startDateTime, endDateTime, pageable);
        JPAQuery<Long> countQuery = countOrderBetweenDates(startDateTime, endDateTime);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }


    private List<Order> fetchOrdersBetweenDates(LocalDate startDateTime, LocalDate endDateTime, Pageable pageable) {
        return queryFactory
            .selectFrom(order)
            .where(startDate(startDateTime), endDate(endDateTime))
            .orderBy(order.registeredDateTime.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    private JPAQuery<Long> countOrderBetweenDates(LocalDate startDateTime, LocalDate endDateTime) {
        return queryFactory
            .select(order.count())
            .from(order)
            .where(startDate(startDateTime), endDate(endDateTime));
    }

    private BooleanExpression startDate(LocalDate startDateTime) {
        if(startDateTime == null) {
            throw new IllegalArgumentException("시작 날짜를 입력해주세요.");
        }

        return order.registeredDateTime.goe(startDateTime.atStartOfDay());
    }

    private BooleanExpression endDate(LocalDate endDateTime) {
        if(endDateTime == null) {
            throw new IllegalArgumentException("마지막 날짜를 입력해주세요.");
        }

        return order.registeredDateTime.loe(endDateTime.plusDays(1).atTime(LocalTime.MAX));
    }
}
