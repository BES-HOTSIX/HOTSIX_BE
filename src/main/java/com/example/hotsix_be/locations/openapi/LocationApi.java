package com.example.hotsix_be.locations.openapi;

import com.example.hotsix_be.common.dto.EmptyResponse;
import com.example.hotsix_be.locations.dto.Response.LocationResponse;
import com.example.hotsix_be.locations.entity.FoodLocation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
/*
@Tag(name = "Location", description = "숙소 근처 편의시설 정보 API")
public interface LocationApi {
    @Operation(summary = "숙소 근처 음식점 정보 조회", description = "숙소 근처 음식점 정보를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "숙소 근처 음식점 정보 조회 성공"
    )
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @Parameter(name = "latitude", description = "숙소의 위도", required = true, example = "37.123456")
    @Parameter(name = "longitude", description = "숙소의 경도", required = true, example = "127.123456")
    @Parameter(name = "distance", description = "숙소와의 거리(m)", required = true, example = "500")
    @GetMapping("/food")
    public List<LocationResponse<FoodLocation>> getFoodLocations(
            @RequestParam(value = "latitude") final Double latitude,
            @RequestParam(value = "longitude") final Double longitude,
            @RequestParam(value = "distance") final Double distance
    );
}
*/