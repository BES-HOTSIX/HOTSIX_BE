package com.example.hotsix_be.hotel.dto.response;

import com.example.hotsix_be.image.entity.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "숙소 사진 조회 응답")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImagesResponse {

    @Schema(description = "숙소 이미지 URL")
    private List<String> imageUrl;

    public static ImagesResponse of(final List<Image> images) {
        return new ImagesResponse(
                images.stream()
                        .map(Image::getUrl)
                        .toList());
    }
}
