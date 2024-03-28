package sample.cafekiosk.spring.domain.order;

import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findOrderBy(LocalDateTime startDateTime, LocalDateTime endDateTime, OrderStatus orderStatus);

    Page<Order> findByOrderDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
