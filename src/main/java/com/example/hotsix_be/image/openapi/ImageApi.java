package com.example.hotsix_be.image.openapi;

import com.example.hotsix_be.common.dto.EmptyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Image", description = "이미지 관리 API")
public interface ImageApi {

    @Operation(
            summary = "이미지 삭제",
            description = "이미지를 삭제를 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "이미지 삭제 성공"
    )
    @ApiResponse(responseCode = "400", description = "이미지 삭제 실패",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @DeleteMapping
    public ResponseEntity<?> deleteImage(@RequestParam("imageUrl") String imageUrl);
}
