package com.example.hotsix_be.payment.pay.openapi;

import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.EmptyResponse;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.payment.cashlog.dto.response.CashLogIdResponse;
import com.example.hotsix_be.payment.payment.dto.request.TossConfirmRequest;
import com.example.hotsix_be.reservation.dto.response.ReservationDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Pay", description = "결제 관리 API")
public interface PayApi {
    @Operation(
            summary = "결제하기 화면 조회",
            description = "isPaid 필드가 false인 예약을 결제하기 위한 화면을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제하기 조회 성공"),
            @ApiResponse(responseCode = "400", description = "결제하기 조회 실패",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class)))})
    ResponseEntity<ResponseDto<ReservationDetailResponse>> showPay(
            final Long reserveId,
            @Parameter(hidden = true) final Accessor accessor
    );

    @Operation(
            summary = "캐시 결제",
            description = "보유하고 있는 캐시만을 통해 결제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "캐시 결제 성공"),
            @ApiResponse(responseCode = "400", description = "캐시 결제 실패",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class)))})
    ResponseEntity<ResponseDto<CashLogIdResponse>> payByCash(
            final Long reserveId
    );

    @Operation(
            summary = "토스페이먼츠 결제",
            description = "토스페이먼츠만을 통해 잔액을 모두 결제하거나 사용하고자하는 캐시를 차감하고 남은 금액을 결제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토스페이먼츠 결제 성공"),
            @ApiResponse(responseCode = "400", description = "토스페이먼츠 결제 실패",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class)))})
    ResponseEntity<ResponseDto<CashLogIdResponse>> payByToss(
            final TossConfirmRequest tossConfirmRequest,
            final Long reserveId
    );
}
