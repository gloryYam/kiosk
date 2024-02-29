package sample.cafekiosk.spring.domain.order;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = 1879840671L;

    public static final QOrder order = new QOrder("order1");

    public final sample.cafekiosk.spring.domain.QBaseEntity _super = new sample.cafekiosk.spring.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDateTime = _super.createDateTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDateTime = _super.modifiedDateTime;

    public final ListPath<sample.cafekiosk.spring.domain.orderproduct.OrderProduct, sample.cafekiosk.spring.domain.orderproduct.QOrderProduct> orderProducts = this.<sample.cafekiosk.spring.domain.orderproduct.OrderProduct, sample.cafekiosk.spring.domain.orderproduct.QOrderProduct>createList("orderProducts", sample.cafekiosk.spring.domain.orderproduct.OrderProduct.class, sample.cafekiosk.spring.domain.orderproduct.QOrderProduct.class, PathInits.DIRECT2);

    public final EnumPath<OrderStatus> orderStatus = createEnum("orderStatus", OrderStatus.class);

    public final DateTimePath<java.time.LocalDateTime> registeredDateTime = createDateTime("registeredDateTime", java.time.LocalDateTime.class);

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    public QOrder(String variable) {
        super(Order.class, forVariable(variable));
    }

    public QOrder(Path<? extends Order> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrder(PathMetadata metadata) {
        super(Order.class, metadata);
    }

}

