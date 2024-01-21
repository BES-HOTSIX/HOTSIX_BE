package com.example.hotsix_be.common.dto;

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
    private boolean result;
    private int status;
    private String success;
    private String error;
    private List<T> listData;
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