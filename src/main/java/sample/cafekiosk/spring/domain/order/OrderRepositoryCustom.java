package sample.cafekiosk.spring.domain.order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findOrderBy(LocalDateTime startDateTime, LocalDateTime endDateTime, OrderStatus orderStatus);
}
