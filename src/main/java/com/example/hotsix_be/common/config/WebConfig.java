package com.example.hotsix_be.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // Next.js 애플리케이션의 URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("X-CSRF-Token", "X-Requested-With", "Accept", "Accept-Version", "Content-Length",
                        "Content-MD5", "Content-Type", "Date", "X-Api-Version")
                .allowCredentials(true);
    }


}
