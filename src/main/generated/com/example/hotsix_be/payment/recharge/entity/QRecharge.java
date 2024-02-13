package com.example.hotsix_be.payment.recharge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecharge is a Querydsl query type for Recharge
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecharge extends EntityPathBase<Recharge> {

    private static final long serialVersionUID = -726479579L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecharge recharge = new QRecharge("recharge");

    public final com.example.hotsix_be.common.entity.QDateEntity _super = new com.example.hotsix_be.common.entity.QDateEntity(this);

    public final StringPath accountNumber = createString("accountNumber");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final DateTimePath<java.time.LocalDateTime> cancelDate = createDateTime("cancelDate", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath depositor = createString("depositor");

    public final EnumPath<com.example.hotsix_be.payment.cashlog.entity.EventType> eventType = createEnum("eventType", com.example.hotsix_be.payment.cashlog.entity.EventType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isPaid = createBoolean("isPaid");

    public final StringPath orderId = createString("orderId");

    public final com.example.hotsix_be.member.entity.QMember recipient;

    public final StringPath secret = createString("secret");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRecharge(String variable) {
        this(Recharge.class, forVariable(variable), INITS);
    }

    public QRecharge(Path<? extends Recharge> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecharge(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecharge(PathMetadata metadata, PathInits inits) {
        this(Recharge.class, metadata, inits);
    }

    public QRecharge(Class<? extends Recharge> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recipient = inits.isInitialized("recipient") ? new com.example.hotsix_be.member.entity.QMember(forProperty("recipient")) : null;
    }

}

