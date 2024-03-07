package com.example.hotsix_be.touristSpot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class TouristSpotService {

    private final String naverApiClientId;
    private final String naverApiClientSecret;

    public TouristSpotService(
            @Value("${search.naver.api.client-id}") String naverApiClientId,
            @Value("${search.naver.api.client-secret}") String naverApiClientSecret) {
        this.naverApiClientId = naverApiClientId;
        this.naverApiClientSecret = naverApiClientSecret;
        System.out.println("naverApiClientId: " + naverApiClientId);
        System.out.println("naverApiClientSecret: " + naverApiClientSecret);
    }

    public String searchTouristSpot(String query) {
        try {
            // Naver API 호출을 위한 URL 및 파라미터 설정
            String url = "https://openapi.naver.com/v1/search/local.json";
            String apiParam = "?query=" + query;

            // HTTP 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", naverApiClientId);
            headers.set("X-Naver-Client-Secret", naverApiClientSecret);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // HTTP 요청 보내기
            ResponseEntity<String> responseEntity = new RestTemplate().getForEntity(url + apiParam, String.class);
            String result = responseEntity.getBody();

            // 결과 반환
            return result;
        } catch (HttpClientErrorException e) {
            // 에러 처리
            return "Error: " + e.getStatusCode() + " - " + e.getStatusText();
        }
    }
}