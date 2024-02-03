package com.example.hotsix_be.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {

    @Schema(description = "성공 여부", example = "true")
    private boolean result;

    @Schema(description = "상태 코드", example = "200")
    private int status;

    @Schema(description = "성공 메시지", example = "성공적으로 요청이 처리되었습니다.")
    private String success;

    @Schema(description = "에러 메시지")
    private String error;

    @Schema(description = "리스트 데이터")
    private List<T> listData;

    @Schema(description = "객체 데이터")
    private T objData;

    public ResponseDto(int status, String success, String error, List<T> listData, T objData) {
        this.result = status >= 200 && status < 400;
        this.status = status;
        this.success = success;
        this.error = error;
        this.listData = listData;
        this.objData = objData;
    }
}