package com.example.hotsix_be.payment.withdraw.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWithdraw is a Querydsl query type for Withdraw
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWithdraw extends EntityPathBase<Withdraw> {

    private static final long serialVersionUID = -1747927061L;

    public static final QWithdraw withdraw = new QWithdraw("withdraw");

    public final StringPath accountNumber = createString("accountNumber");

    public final StringPath bankCode = createString("bankCode");

    public final DateTimePath<java.time.LocalDateTime> cancelDate = createDateTime("cancelDate", java.time.LocalDateTime.class);

    public QWithdraw(String variable) {
        super(Withdraw.class, forVariable(variable));
    }

    public QWithdraw(Path<? extends Withdraw> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWithdraw(PathMetadata metadata) {
        super(Withdraw.class, metadata);
    }

}

