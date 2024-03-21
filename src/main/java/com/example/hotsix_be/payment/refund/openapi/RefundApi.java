package com.example.hotsix_be.payment.refund.openapi;

import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.EmptyResponse;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.coupon.dto.request.DiscountAmountRequest;
import com.example.hotsix_be.payment.cashlog.dto.response.CashLogIdResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Refund", description = "환불 관리 API")
public interface RefundApi {
    @Operation(
            summary = "예약 취소",
            description = "예약을 취소하고 해당 Reservation 객체의 cancelDate 값을 현재 시간으로 입력합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "예약 취소 성공"),
            @ApiResponse(responseCode = "400", description = "예약 취소 실패",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class)))})
    ResponseEntity<ResponseDto<CashLogIdResponse>> cancelReservation(
            final Long reserveId,
            @Parameter(hidden = true) final Accessor accessor,
            final DiscountAmountRequest discountAmountRequest
    );
}
