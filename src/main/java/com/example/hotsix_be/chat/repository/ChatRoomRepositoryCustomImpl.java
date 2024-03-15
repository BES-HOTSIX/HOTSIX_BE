package com.example.hotsix_be.chat.repository;

import com.example.hotsix_be.chat.entity.ChatRoom;
import com.example.hotsix_be.chat.entity.QChatRoom;
import com.example.hotsix_be.chat.entity.QMessage;
import com.example.hotsix_be.member.entity.Member;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {
	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<ChatRoom> findAvailableChatRoomsByHostOrUserWithLatestMessage(Pageable pageable, Member member) {
		JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);
		QChatRoom qChatRoom = QChatRoom.chatRoom;
		QMessage qMessage = QMessage.message;

		BooleanExpression isHostOrUser = qChatRoom.host.eq(member).or(qChatRoom.user.eq(member));
		BooleanExpression isNotLeft = qChatRoom.isLeft.eq(false);
		BooleanExpression whereCondition = isHostOrUser.and(isNotLeft);

		List<ChatRoom> chatRooms = jpaQueryFactory
				.selectFrom(qChatRoom)
				.leftJoin(qChatRoom.messages, qMessage)
				.where(whereCondition)
				.groupBy(qChatRoom.id)
				.orderBy(qMessage.createdAt.max().desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		long total = jpaQueryFactory
				.selectFrom(qChatRoom)
				.where(whereCondition)
				.fetchCount();

		return new PageImpl<>(chatRooms, pageable, total);
	}

	@Override
	public Page<ChatRoom> findExitedChatRoomsByHostWithLatestMessage(Pageable pageable, Member member) {
		JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);
		QChatRoom qChatRoom = QChatRoom.chatRoom;
		QMessage qMessage = QMessage.message;

		BooleanExpression isHost = qChatRoom.host.eq(member);
		BooleanExpression isLeft = qChatRoom.isLeft.eq(true);
		BooleanExpression whereCondition = isHost.and(isLeft);

		List<ChatRoom> chatRooms = jpaQueryFactory
				.selectFrom(qChatRoom)
				.leftJoin(qChatRoom.messages, qMessage)
				.where(whereCondition)
				.groupBy(qChatRoom.id)
				.orderBy(qMessage.createdAt.max().desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		long total = jpaQueryFactory
				.selectFrom(qChatRoom)
				.where(whereCondition)
				.fetchCount();

		return new PageImpl<>(chatRooms, pageable, total);
	}
}
