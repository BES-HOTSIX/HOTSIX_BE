package com.example.hotsix_be.image.controller;

import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {
    private final ImageService imageService;

    @DeleteMapping
    public ResponseEntity<?> deleteImage(@RequestParam("imageUrl") String imageUrl) {
        String imageId = imageService.getImageIdAndDeleteImage(
                imageUrl); // ImageRepository 이미지 삭제 및 S3 버켓 이미지 ID 반환
        imageService.deleteImageInS3Bucket(imageId); // S3 버켓 이미지 삭제

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "이미지 삭제가 성공적으로 완료되었습니다.", null,
                        null, null
                )
        );
    }
}
