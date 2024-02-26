package com.example.hotsix_be.payment.recharge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRecharge is a Querydsl query type for Recharge
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecharge extends EntityPathBase<Recharge> {

    private static final long serialVersionUID = -726479579L;

    public static final QRecharge recharge = new QRecharge("recharge");

    public final StringPath accountNumber = createString("accountNumber");

    public final DateTimePath<java.time.LocalDateTime> cancelDate = createDateTime("cancelDate", java.time.LocalDateTime.class);

    public final BooleanPath cancelled = createBoolean("cancelled");

    public final StringPath depositor = createString("depositor");

    public final StringPath secret = createString("secret");

    public QRecharge(String variable) {
        super(Recharge.class, forVariable(variable));
    }

    public QRecharge(Path<? extends Recharge> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRecharge(PathMetadata metadata) {
        super(Recharge.class, metadata);
    }

}

