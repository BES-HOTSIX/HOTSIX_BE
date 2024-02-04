package com.example.hotsix_be.common.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Home", description = "어플리케이션 정상 작동 확인 API")
@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "hello";
    }

}