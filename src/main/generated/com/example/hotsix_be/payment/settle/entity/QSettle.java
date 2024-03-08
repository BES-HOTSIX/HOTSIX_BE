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

    public final NumberPath<Long> commission = createNumber("commission", Long.class);

    public final NumberPath<Integer> rateOfCommission = createNumber("rateOfCommission", Integer.class);

    public final NumberPath<Long> totalAmount = createNumber("totalAmount", Long.class);

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

