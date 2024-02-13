package com.example.hotsix_be.payment.withdraw.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWithdraw is a Querydsl query type for Withdraw
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWithdraw extends EntityPathBase<Withdraw> {

    private static final long serialVersionUID = -1747927061L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWithdraw withdraw = new QWithdraw("withdraw");

    public final com.example.hotsix_be.common.entity.QDateEntity _super = new com.example.hotsix_be.common.entity.QDateEntity(this);

    public final com.example.hotsix_be.member.entity.QMember applicant;

    public final StringPath bankAccountNo = createString("bankAccountNo");

    public final StringPath bankName = createString("bankName");

    public final DateTimePath<java.time.LocalDateTime> cancelDate = createDateTime("cancelDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> cash = createNumber("cash", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath msg = createString("msg");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final DateTimePath<java.time.LocalDateTime> withdrawDate = createDateTime("withdrawDate", java.time.LocalDateTime.class);

    public QWithdraw(String variable) {
        this(Withdraw.class, forVariable(variable), INITS);
    }

    public QWithdraw(Path<? extends Withdraw> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWithdraw(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWithdraw(PathMetadata metadata, PathInits inits) {
        this(Withdraw.class, metadata, inits);
    }

    public QWithdraw(Class<? extends Withdraw> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.applicant = inits.isInitialized("applicant") ? new com.example.hotsix_be.member.entity.QMember(forProperty("applicant")) : null;
    }

}

