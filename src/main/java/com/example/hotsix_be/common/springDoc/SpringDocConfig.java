package com.example.hotsix_be.common.springDoc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@OpenAPIDefinition(info = @Info(title = "HOTSHARE-API", version = "V1"))
//public class SpringDocConfig {
//    @Bean
//    public GroupedOpenApi groupApiV1() {
//        return GroupedOpenApi.builder()
//                .group("apiV1")
//                .pathsToMatch("/api/v1/**")
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi apiGroupOthers() {
//        return GroupedOpenApi.builder()
//                .group("group3")
//                .pathsToMatch("/**")
//                .build();
//    }
//
//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .components(new Components()
//                        .addSecuritySchemes("Authorization",
//                                new SecurityScheme()
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .in(SecurityScheme.In.HEADER)
//                                        .scheme("Bearer")
//                                        .bearerFormat("JWT")));
//    }
//}