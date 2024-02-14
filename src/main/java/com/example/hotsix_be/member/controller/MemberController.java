package com.example.hotsix_be.member.controller;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.hotel.dto.response.HotelDetailResponse;
import com.example.hotsix_be.hotel.service.HotelService;
import com.example.hotsix_be.like.service.LikeService;
import com.example.hotsix_be.member.dto.request.MemberPasswordChangeRequest;
import com.example.hotsix_be.member.dto.request.MemberRegisterRequest;
import com.example.hotsix_be.member.dto.response.MemberInfoResponse;
import com.example.hotsix_be.member.openapi.MemberApi;
import com.example.hotsix_be.member.service.MemberService;
import com.example.hotsix_be.reservation.dto.response.ReservationDetailResponse;
import com.example.hotsix_be.reservation.service.ReservationService;
import com.example.hotsix_be.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController implements MemberApi {

    private final MemberService memberService;
    private final ReservationService reservationService;
    private final HotelService hotelService;
    private final LikeService likeService;
    private final ReviewService reviewService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<?>> registerMember(@RequestBody MemberRegisterRequest memberRegisterRequest) {

        log.info("memberRegisterRequest: " + memberRegisterRequest.getNickname());

        memberService.save(memberRegisterRequest);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.CREATED.value(),
                        "회원가입이 성공적으로 완료되었습니다.", null,
                        null, null
                )
        );
    }

    @GetMapping("/info")
    @MemberOnly
    public ResponseEntity<ResponseDto<MemberInfoResponse>> getMemberInfo(@Auth final Accessor accessor) {

        MemberInfoResponse memberInfo = memberService.getMemberInfo(accessor.getMemberId());

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "회원정보 조회가 성공적으로 완료되었습니다.", null,
                        null, memberInfo
                )
        );
    }

    @PutMapping("/password")
    @MemberOnly
    public ResponseEntity<ResponseDto<?>> changePassword(
            @RequestBody MemberPasswordChangeRequest memberPasswordChangeRequest,
            @Auth final Accessor accessor
    ) {
        String password = memberPasswordChangeRequest.getPassword();
        String passwordCheck = memberPasswordChangeRequest.getPasswordCheck();

        if (!password.equals(passwordCheck)) {
            return ResponseEntity.badRequest().body(
                    new ResponseDto<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "비밀번호가 일치하지 않습니다.", null,
                            null, null
                    )
            );
        }

        memberService.changePassword(accessor.getMemberId(), password);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "비밀번호 변경이 성공적으로 완료되었습니다.", null,
                        null, null
                )
        );
    }

    @GetMapping("/me/reservations")
    @MemberOnly
    public ResponseEntity<ResponseDto<Page<ReservationDetailResponse>>> getMyReservations(
            @Auth final Accessor accessor,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        Page<ReservationDetailResponse> reservationDetailResponses = reservationService.findByMemberIdAndIsPaid(accessor.getMemberId(), page);
        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "예약 조회가 성공적으로 완료되었습니다.", null,
                        null, reservationDetailResponses
                )
        );
    }

    @GetMapping("/me/hotels")
    @MemberOnly
    public ResponseEntity<ResponseDto<Page<HotelDetailResponse>>> getMyHotels(
            @Auth final Accessor accessor,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {

        Page<HotelDetailResponse> hotelDetailResponses = hotelService.findByMemberId(accessor.getMemberId(), page);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "예약 조회가 성공적으로 완료되었습니다.", null,
                        null, hotelDetailResponses
                )
        );
    }

    @GetMapping("/me/likes")
    @MemberOnly
    public ResponseEntity<ResponseDto<Page<HotelDetailResponse>>> getMyLikes(
            @Auth final Accessor accessor,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        Page<HotelDetailResponse> hotelDetailResponses = likeService.findLikedHotelsByMemberId(accessor.getMemberId(), page);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "찜한 숙소 조회가 성공적으로 완료되었습니다.", null,
                        null, hotelDetailResponses
                )
        );
    }

    @GetMapping("/nickname/exists")
    public ResponseEntity<ResponseDto<?>> isExistNickname(@RequestParam("nickname") String nickname) {
        boolean isExist = memberService.isExistNickname(nickname);

        if (isExist) {
            return ResponseEntity.badRequest().body(
                    new ResponseDto<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "이미 존재하는 닉네임입니다.", null,
                            null, null
                    )
            );
        }

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "닉네임 중복 확인이 성공적으로 완료되었습니다.", null,
                        null, isExist
                )
        );
    }

    @PutMapping("/nickname")
    @MemberOnly
    public ResponseEntity<ResponseDto<?>> changeNickName(
            @Auth final Accessor accessor,
            @RequestParam("nickname") String nickname
    ) {
        if (memberService.isExistNickname(nickname)) {
            return ResponseEntity.badRequest().body(
                    new ResponseDto<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "이미 존재하는 닉네임입니다.", null,
                            null, null
                    )
            );
        }

        memberService.changeNickname(accessor.getMemberId(), nickname);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "닉네임 변경이 성공적으로 완료되었습니다.", null,
                        null, null
                )
        );
    }

    @PutMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @MemberOnly
    public ResponseEntity<ResponseDto<?>> changeImageUrl(
            @Auth final Accessor accessor,
            @RequestPart(value = "files", required = false) final List<MultipartFile> multipartFiles
    ) {
        memberService.changeImageUrl(accessor.getMemberId(), multipartFiles);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "프로필 사진 변경이 성공적으로 완료되었습니다.", null,
                        null, null
                )
        );
    }
}
