package com.example.hotsix_be.payment.settle.openapi;

import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.EmptyResponse;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.payment.settle.dto.response.MySettleResponse;
import com.example.hotsix_be.payment.settle.dto.response.ReservationForSettleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

@Tag(name = "Settle", description = "정산 관리 API")
public interface SettleApi {

    @Operation(
            summary = "내 정산 정보 조회",
            description = "현재 로그인한 회원의 정산 정보 ( 보유 캐시, 다음 정산 예정일, 정산 예정 금액 ) 을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 정산 정보 조회 성공"),
            @ApiResponse(responseCode = "400", description = "내 정산 정보 조회 실패",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class)))})
    ResponseEntity<ResponseDto<MySettleResponse>> showSettlePage(@Parameter(hidden = true) Accessor accessor);

    @Operation(
            summary = "내 정산 내역 조회",
            description = "현재 로그인한 회원의 정산 내역을 페이지로 가져옵니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 정산 내역 조회 성공"),
            @ApiResponse(responseCode = "400", description = "내 정산 내역 조회 실패",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = EmptyResponse.class)))})
    @PageableAsQueryParam
    ResponseEntity<ResponseDto<Page<ReservationForSettleResponse>>> showSettleList(
            @Parameter(hidden = true) Accessor accessor,
            final Pageable pageable,
            LocalDate startDate,
            LocalDate endDate,
            String settleKw
    );
}
