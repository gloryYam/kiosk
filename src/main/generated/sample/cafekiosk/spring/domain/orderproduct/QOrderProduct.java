package sample.cafekiosk.spring.domain.orderproduct;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderProduct is a Querydsl query type for OrderProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderProduct extends EntityPathBase<OrderProduct> {

    private static final long serialVersionUID = -1360987617L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderProduct orderProduct = new QOrderProduct("orderProduct");

    public final sample.cafekiosk.spring.domain.QBaseEntity _super = new sample.cafekiosk.spring.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDateTime = _super.createDateTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDateTime = _super.modifiedDateTime;

    public final sample.cafekiosk.spring.domain.order.QOrder order;

    public final sample.cafekiosk.spring.domain.product.QProduct product;

    public QOrderProduct(String variable) {
        this(OrderProduct.class, forVariable(variable), INITS);
    }

    public QOrderProduct(Path<? extends OrderProduct> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderProduct(PathMetadata metadata, PathInits inits) {
        this(OrderProduct.class, metadata, inits);
    }

    public QOrderProduct(Class<? extends OrderProduct> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.order = inits.isInitialized("order") ? new sample.cafekiosk.spring.domain.order.QOrder(forProperty("order")) : null;
        this.product = inits.isInitialized("product") ? new sample.cafekiosk.spring.domain.product.QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

