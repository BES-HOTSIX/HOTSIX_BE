package com.example.hotsix_be.payment.recharge.openapi;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.EmptyResponse;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.payment.payment.dto.request.TossConfirmRequest;
import com.example.hotsix_be.payment.payment.dto.request.TossWebhookRequest;
import com.example.hotsix_be.payment.recharge.dto.response.RechargeConfirmResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Recharge", description = "캐시 충전 관리 API")
public interface RechargeApi {

    @Operation(
            summary = "내 충전 신청 내역 페이지 조회",
            description = "현재 로그인한 회원의 캐시 충전 신청 내역을 Recharge 엔티티를 기반으로 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 충전 신청 내역 페이지 조회 성공"),
            @ApiResponse(responseCode = "400", description = "내 충전 신청 내역 페이지 조회 실패",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class)))})
    @PageableAsQueryParam
    ResponseEntity<ResponseDto<PageImpl<RechargeConfirmResponse>>> showMyRecharge(
            final Pageable pageable,
            @Auth final Accessor accessor
    );

    @Operation(
            summary = "토스페이먼츠 충전 신청",
            description = "토스페이먼츠 api를 활용한 충전 신청을 합니다. RequestBody 값을 활용해 " +
                    "토스 서버에 요청을 보내고 반환된 응답을 TossPaymentRequest 에 담아 캐시 충전을 진행합니다." +
                    "TossPaymentRequest 내의 method 에 따라 다른 방식의 충전을 진행합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200-1", description = "가상계좌 충전 신청 성공"),
            @ApiResponse(responseCode = "200-2", description = "간편결제 충전 성공"),
            @ApiResponse(responseCode = "400", description = "토스페이먼츠 충전 신청 실패",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    })
    ResponseEntity<ResponseDto<EmptyResponse>> recharge(
            @RequestBody final TossConfirmRequest tossConfirmRequest,
            @Auth final Accessor accessor
    );

    @Operation(
            summary = "가상계좌 충전 신청 취소 토스페이먼츠 웹훅",
            description = "가상계좌 충전 신청 취소를 위한 웹훅입니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "충전 신청 취소 웹훅 성공"),
            @ApiResponse(responseCode = "400", description = "충전 신청 취소 웹훅 실패",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    })
    ResponseEntity<ResponseDto<EmptyResponse>> tossWebhook(
            @RequestBody TossWebhookRequest tossWebhookRequest
    );

    @Operation(
            summary = "가상계좌 충전 신청 취소",
            description = "가상계좌 충전 신청 취소를 위한 엔드포인트입니다. 웹훅과 연동하여 사용됩니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "충전 신청 취소 성공"),
            @ApiResponse(responseCode = "400", description = "충전 신청 취소 실패",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    })
    ResponseEntity<ResponseDto<EmptyResponse>> cancelRecharge(
            @PathVariable String orderId,
            @Auth Accessor accessor
    );
}
