package com.example.hotsix_be.payment.settle.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSettle is a Querydsl query type for Settle
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSettle extends EntityPathBase<Settle> {

    private static final long serialVersionUID = -1817124499L;

    public static final QSettle settle = new QSettle("settle");

    public final StringPath accountNumber = createString("accountNumber");

    public final NumberPath<Long> actualAmount = createNumber("actualAmount", Long.class);

    public final StringPath bankCode = createString("bankCode");

    public final NumberPath<Integer> commission = createNumber("commission", Integer.class);

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public QSettle(String variable) {
        super(Settle.class, forVariable(variable));
    }

    public QSettle(Path<? extends Settle> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSettle(PathMetadata metadata) {
        super(Settle.class, metadata);
    }

}

