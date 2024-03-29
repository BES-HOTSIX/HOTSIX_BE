package com.example.hotsix_be.payment.cashlog.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCashLog is a Querydsl query type for CashLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCashLog extends EntityPathBase<CashLog> {

    private static final long serialVersionUID = 1032545469L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCashLog cashLog = new QCashLog("cashLog");

    public final com.example.hotsix_be.common.entity.QDateEntity _super = new com.example.hotsix_be.common.entity.QDateEntity(this);

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath dtype = createString("dtype");

    public final EnumPath<EventType> eventType = createEnum("eventType", EventType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.hotsix_be.member.entity.QMember member;

    public final StringPath orderId = createString("orderId");

    public final DateTimePath<java.time.LocalDateTime> payDate = createDateTime("payDate", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCashLog(String variable) {
        this(CashLog.class, forVariable(variable), INITS);
    }

    public QCashLog(Path<? extends CashLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCashLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCashLog(PathMetadata metadata, PathInits inits) {
        this(CashLog.class, metadata, inits);
    }

    public QCashLog(Class<? extends CashLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.hotsix_be.member.entity.QMember(forProperty("member")) : null;
    }

}

