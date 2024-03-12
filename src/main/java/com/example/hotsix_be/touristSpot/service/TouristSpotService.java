package com.example.hotsix_be.touristSpot.service;

import com.example.hotsix_be.touristSpot.dto.response.TouristSpotResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TouristSpotService {

    @Value("${search.naver.client-id}")
    private final String naverApiClientId;
    @Value("${search.naver.client-secret}")
    private final String naverApiClientSecret;

    public TouristSpotService(
            @Value("${search.naver.client-id}") String naverApiClientId,
            @Value("${search.naver.client-secret}") String naverApiClientSecret) {
        this.naverApiClientId = naverApiClientId;
        this.naverApiClientSecret = naverApiClientSecret;
    }

    public Object searchTouristSpot(String query) {
        try {
            // 띄어쓰기를 기준으로 도시 이름 추출
            String city = query.split(" ")[0];
            String eventKeyword = city + " 축제";

            String url = "https://openapi.naver.com/v1/search/local.json";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("query", eventKeyword)
                    .queryParam("display", 5);
            String apiUrl = builder.toUriString();

            // HTTP 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", naverApiClientId);
            headers.set("X-Naver-Client-Secret", naverApiClientSecret);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // HTTP 요청 보내기
            RequestEntity<?> requestEntity = RequestEntity
                    .get(new URI(apiUrl))
                    .headers(headers)
                    .build();

            ResponseEntity<String> responseEntity = new RestTemplate().exchange(requestEntity, String.class);
            String response = responseEntity.getBody();

            System.out.println("eventKeyword: " + eventKeyword);

            System.out.println("API Response: " + response);

            // JSON 문자열을 TouristSpotResponse 객체로 매핑
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            List<TouristSpotResponse> touristSpots = objectMapper.readValue(jsonNode.get("items").toString(), new TypeReference<List<TouristSpotResponse>>() {});

            // 결과 반환
            return touristSpots;
        } catch (HttpClientErrorException e) {
            // 에러 처리
            System.out.println("HTTP Error: " + e.getStatusCode() + " - " + e.getStatusText());
            return Collections.emptyList();
        } catch (IOException e) {
            // JSON 매핑 에러 처리
            System.out.println("Error parsing JSON response");
            return Collections.emptyList();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}