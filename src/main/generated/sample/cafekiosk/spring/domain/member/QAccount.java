package sample.cafekiosk.spring.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAccount is a Querydsl query type for Account
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccount extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1189201401L;

    public static final QAccount account = new QAccount("account");

    public final sample.cafekiosk.spring.domain.QBaseEntity _super = new sample.cafekiosk.spring.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDateTime = _super.createDateTime;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDateTime = _super.modifiedDateTime;

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final EnumPath<MemberRole> userRole = createEnum("userRole", MemberRole.class);

    public QAccount(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QAccount(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccount(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

