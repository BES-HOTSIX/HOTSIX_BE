package com.example.hotsix_be.payment.cashlog.openapi;

import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.EmptyResponse;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.payment.cashlog.dto.response.ConfirmResponse;
import com.example.hotsix_be.payment.cashlog.dto.response.MyCashLogResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "CashLog", description = "캐시 관리 API")
public interface CashLogApi {

    @Operation(
            summary = "내 캐시 내역 페이지 조회",
            description = "현재 로그인한 회원의 캐시 입출금 내역을 CashLog 엔티티를 기반으로 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 캐시 사용 내역 조회 성공"),
            @ApiResponse(responseCode = "400", description = "내 캐시 사용 내역 조회 실패",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class)))})
    @PageableAsQueryParam
    ResponseEntity<ResponseDto<MyCashLogResponse>> showMyCashLogs(
            final Pageable pageable,
            @Parameter(hidden = true) final Accessor accessor
    );

    @Operation(
            summary = "예약 결제, 예약 취소 확인",
            description = "예약 결제나 예약을 취소할 경우 해당 내용을 확인하는 페이지를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "예약 확인 조회 성공"),
            @ApiResponse(responseCode = "400", description = "예약 확인 조회 실패",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class)))})
    ResponseEntity<ResponseDto<ConfirmResponse>> showConfirm(
            final Long cashLogId,
            @Parameter(hidden = true) final Accessor accessor
    );
}
