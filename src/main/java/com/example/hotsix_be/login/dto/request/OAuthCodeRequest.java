package com.example.hotsix_be.login.dto.request;

import static lombok.AccessLevel.PRIVATE;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "OAuth Code 요청")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class OAuthCodeRequest {

    @Schema(description = "OAuth Code", example = "4/0AY0e-g7z3z")
    private String code;

    @Schema(description = "state", example = "uuid")
    private String state = null;
}
