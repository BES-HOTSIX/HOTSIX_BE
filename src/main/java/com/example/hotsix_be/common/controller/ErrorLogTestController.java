package com.example.hotsix_be.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Error", description = "에러 로그 테스트 API")
@RestController
public class ErrorLogTestController {

    @Operation(
            summary = "에러 테스트",
            description = "에러를 발생시켜 슬랙에 에러 로그가 보내지는 지 테스트합니다."
    )
    @ApiResponse(
            responseCode = "500",
            description = "에러 발생 성공"
    )
    @GetMapping("/test-error")
    public String testError() {
        try {
            // 의도적으로 예외를 발생시킵니다.
            throw new RuntimeException("슬랙에 메세지가 보내지는 지 테스트하기 위해 의도적으로 에러 발생");
        } catch (RuntimeException e) {
            // 에러 로그를 기록합니다.
            log.error("에러가 발생하였습니다.: {}", e.getMessage(), e);
        }
        return "에러가 발생하였습니다.";
    }
}
