package com.example.hotsix_be.chat.service;

import com.example.hotsix_be.chat.dto.request.ChatMessageRequest;
import com.example.hotsix_be.chat.dto.request.ChatRoomCreateRequest;
import com.example.hotsix_be.chat.dto.response.*;
import com.example.hotsix_be.chat.entity.ChatRoom;
import com.example.hotsix_be.chat.entity.Message;
import com.example.hotsix_be.chat.exception.ChatException;
import com.example.hotsix_be.chat.repository.ChatRoomRepository;
import com.example.hotsix_be.chat.repository.MessageRepository;
import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.exception.HotelException;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.hotsix_be.common.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
	private final MemberRepository memberRepository;
	private final HotelRepository hotelRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final MessageRepository messageRepository;

	@Transactional
	public ChatRoomCreateResponse saveChatRoom(final ChatRoomCreateRequest chatRoomCreateRequest, final Long memberId) {
		Member user = memberRepository.findById(memberId).orElseThrow(() -> new AuthException(INVALID_AUTHORITY));

		Hotel hotel = hotelRepository.findById(chatRoomCreateRequest.getHotelId()).orElseThrow(() -> new HotelException(NOT_FOUND_HOTEL_ID));

		ChatRoom chatRoom = chatRoomRepository.findByHostId(hotel.getOwner().getId()).orElse(null);

		if (chatRoom == null) {
			ChatRoom chatRoomResult = chatRoomRepository.save(
					new ChatRoom(
						hotel.getOwner(),
						user
					)
			);

			return ChatRoomCreateResponse.of(chatRoomResult);
		}

		if (!(memberId.equals(chatRoom.getHost().getId()) || memberId.equals(chatRoom.getUser().getId())))
			throw new AuthException(INVALID_AUTHORITY);

		return ChatRoomCreateResponse.of(chatRoom);
	}

	public ChatRoomInfoResponse getChatRoom(final Long roomId, final Long memberId) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new ChatException(NOT_FOUND_CHATROOM_ID));

		memberRepository.findById(memberId).orElseThrow(() -> new AuthException(INVALID_AUTHORITY));

		if (!(memberId.equals(chatRoom.getHost().getId()) || memberId.equals(chatRoom.getUser().getId())))
			throw new AuthException(INVALID_AUTHORITY);

		return ChatRoomInfoResponse.of(chatRoom);
	}

	@Transactional
	public ChatMessageResponse saveMessage(final Long roomId, final ChatMessageRequest chatMessageRequest) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new ChatException(NOT_FOUND_CHATROOM_ID));

		Member sender = memberRepository.findByNickname(chatMessageRequest.getSender()).orElseThrow(() -> new AuthException(INVALID_AUTHORITY));

		Message message = new Message(
				chatRoom,
				sender,
				chatMessageRequest.getContent()
		);

		Message messageResult = messageRepository.save(message);

		return ChatMessageResponse.of(messageResult);
	}

	public MessagesResponse getMessages(final Long roomId, final Long memberId) {
		chatRoomRepository.findById(roomId).orElseThrow(() -> new ChatException(NOT_FOUND_CHATROOM_ID));

		memberRepository.findById(memberId).orElseThrow(() -> new AuthException(INVALID_AUTHORITY));

		List<Message> messageList = messageRepository.findAllByChatRoomId(roomId);

		return MessagesResponse.of(messageList);
	}

	public Page<MemberChatRoomResponse> getMemberChatRooms(final int page, final Long memberId) {
		Pageable pageable = Pageable.ofSize(4).withPage(page);

		Member member = memberRepository.findById(memberId).orElseThrow(() -> new AuthException(INVALID_AUTHORITY));

		return chatRoomRepository.findByHostOrUserOrderByIdDesc(pageable, member, member)
				.map(chatRoom -> {
					Member contact = chatRoom.getHost().equals(member) ? chatRoom.getUser() : chatRoom.getHost();
					LocalDateTime latestDate = messageRepository.findFirstByChatRoomIdOrderByCreatedAtDesc(chatRoom.getId())
							.map(Message::getCreatedAt).orElse(null);

					return MemberChatRoomResponse.of(
							chatRoom,
							contact,
							latestDate
					);
				});
	}
}
