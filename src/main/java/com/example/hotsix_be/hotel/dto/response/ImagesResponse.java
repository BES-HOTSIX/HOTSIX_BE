package com.example.hotsix_be.hotel.dto.response;

import com.example.hotsix_be.image.entity.Image;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImagesResponse {

    private List<String> imageUrl;

    public static ImagesResponse of(final List<Image> images) {
        return new ImagesResponse(
                images.stream()
                        .map(Image::getUrl)
                        .toList());
    }
}
