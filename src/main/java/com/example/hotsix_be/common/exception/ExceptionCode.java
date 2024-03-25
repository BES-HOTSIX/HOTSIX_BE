package com.example.hotsix_be.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    INVALID_REQUEST(1000, "올바르지 않은 요청입니다."),
    FAIL_TO_GENERATE_RANDOM_NICKNAME(1001, "랜덤한 닉네임을 생성하는데 실패하였습니다."),


    INSUFFICIENT_DEPOSIT(2001, "예치금이 부족합니다."),
    FAIL_APPROVE_PURCHASE(2002, "결제 승인에 실패하였습니다."),
    PAYMENT_API_CALL_FAILED(2003, "토스페이먼츠 API 호출에 실패하였습니다."),
    PRICE_NOT_MATCH(2004, "실제 가격과 요청한 금액이 일치하지 않습니다."),
    NOT_FOUND_CASHLOG_ID(2005, "요청한 ID에 해당하는 캐시 사용 내역이 존재하지 않습니다."),
    NOT_FOUND_RECHARGE_ID(2006, "요청한 ID에 해당하는 캐시 충전 신청이 존재하지 않습니다."),
    CANCELLATION_NOT_POSSIBLE(2007, "이미 결제가 완료되어 취소가 불가능합니다."),
    PAYMENT_NOT_POSSIBLE(2008, "이미 취소가 완료되어 결제가 불가능합니다."),
    ALREADY_BEEN_INITIALIZED(2009, "이미 초기화된 CashLog 입니다."),
    ALREADY_BEEN_SETTLED(2010, "이미 정산된 예약입니다."),

    NOT_FIRST_RESERVATION(3001, "첫 예약이 아닙니다."),
    ALREADY_ISSUED_FIRST_RESERVATION_COUPON(3002, "이미 첫 예약 쿠폰을 발행받으셨습니다."),
    ALREADY_USED_FIRST_RESERVATION_COUPON(3003, "이미 쿠폰을 사용하셨습니다."),
    NOT_FOUND_COUPON_TYPE(3004, "요청한 CouponType에 해당하는 쿠폰을 멤버가 소유하고 있지 않습니다."),

    NOT_FOUND_CHATROOM_ID(4001, "요청한 ID에 해당하는 채팅방이 존재하지 않습니다."),

    EXCEED_IMAGE_CAPACITY(5001, "업로드 가능한 이미지 용량을 초과했습니다."),
    NULL_IMAGE(5002, "업로드한 이미지 파일이 NULL입니다."),
    EMPTY_IMAGE_LIST(5003, "최소 한 장 이상의 이미지를 업로드해야합니다."),
    EXCEED_IMAGE_LIST_SIZE(5004, "업로드 가능한 이미지 개수를 초과했습니다."),
    INVALID_IMAGE_URL(5005, "요청한 이미지 URL의 형식이 잘못되었습니다."),
    IMAGE_NOT_FOUND(5006, "해당 이미지가 존재하지 않습니다."),
    INVALID_IMAGE_PATH(5101, "이미지를 저장할 경로가 올바르지 않습니다."),
    FAIL_IMAGE_NAME_HASH(5102, "이미지 이름을 해싱하는 데 실패했습니다."),
    INVALID_IMAGE(5103, "올바르지 않은 이미지 파일입니다."),
    NCP_S3_UPLOAD_FAIL(5104, "NCP S3 업로드 중 에러가 발생했습니다"),
    IMAGE_UPLOAD_IO_ERROR(5105, "이미지 저장 중 IO 에러가 발생했습니다"),


    NOT_FOUND_HOTEL_ID(6001, "요청한 ID에 해당하는 호텔이 존재하지 않습니다."),

    NOT_FOUND_RESERVATION_ID(7001, "요청한 ID에 해당하는 예약 내역이 존재하지 않습니다."),
    INVALID_RESERVATION_REQUEST(7002, "요청에 해당하는 예약 내역을 불러올 수 없습니다."),
    CANCELLATION_PERIOD_EXPIRED(7003, "예약을 취소할 수 있는 기간이 만료되었습니다."),

    NOT_FOUND_REVIEW_ID(8001, "해당 ID의 리뷰를 찾을 수 없습니다."),
    NOT_FOUND_REVIEW_USERNAME(8001, "해당 username의 리뷰를 찾을 수 없습니다."),

    INVALID_AUTHORIZATION_CODE(9001, "유효하지 않은 인증 코드입니다."),
    NOT_SUPPORTED_OAUTH_SERVICE(9002, "해당 OAuth 서비스는 제공하지 않습니다."),
    FAIL_TO_CONVERT_URL_PARAMETER(9003, "Url Parameter 변환 중 오류가 발생했습니다."),
    INVALID_REFRESH_TOKEN(9101, "올바르지 않은 형식의 RefreshToken입니다."),
    INVALID_ACCESS_TOKEN(9102, "올바르지 않은 형식의 AccessToken입니다."),
    EXPIRED_PERIOD_REFRESH_TOKEN(9103, "기한이 만료된 RefreshToken입니다."),
    EXPIRED_PERIOD_ACCESS_TOKEN(9104, "기한이 만료된 AccessToken입니다."),
    FAIL_TO_VALIDATE_TOKEN(9105, "토큰 유효성 검사 중 오류가 발생했습니다."),
    NOT_FOUND_REFRESH_TOKEN(9106, "refresh-token에 해당하는 쿠키 정보가 없습니다."),
    INVALID_AUTHORITY(9201, "해당 요청에 대한 접근 권한이 없습니다."),
    NOT_FOUND_MEMBER_BY_ID(9202, "해당 ID에 해당하는 회원이 존재하지 않습니다."),
    NOT_FOUND_MEMBER_BY_USERNAME(9203, "해당 username에 해당하는 회원이 존재하지 않습니다."),
    PASSWORD_NOT_MATCHED(9204, "비밀번호가 일치하지 않습니다."),

    INTERNAL_SEVER_ERROR(9999, "서버 에러가 발생하였습니다. 관리자에게 문의해 주세요.");

    private final int code;
    private final String message;
}
