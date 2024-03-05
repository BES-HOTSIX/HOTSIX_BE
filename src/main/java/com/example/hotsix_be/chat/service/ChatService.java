package com.example.hotsix_be.chat.service;

import com.example.hotsix_be.chat.dto.request.ChatRoomCreateRequest;
import com.example.hotsix_be.chat.dto.response.ChatRoomCreateResponse;
import com.example.hotsix_be.chat.entity.ChatRoom;
import com.example.hotsix_be.chat.repository.ChatRoomRepository;
import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.exception.HotelException;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.hotsix_be.common.exception.ExceptionCode.INVALID_AUTHORITY;
import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_HOTEL_ID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
	private final MemberRepository memberRepository;
	private final HotelRepository hotelRepository;
	private final ChatRoomRepository chatRoomRepository;

	@Transactional
	public ChatRoomCreateResponse save(final ChatRoomCreateRequest chatRoomCreateRequest, final Long memberId) {
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

		return ChatRoomCreateResponse.of(chatRoom);
	}
}
