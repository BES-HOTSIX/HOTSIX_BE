package com.example.hotsix_be.image.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ncp.cloud.aws")
@Getter
@Setter
public class NcpS3Properties {
    private Credentials credentials;
    private S3 s3;

    @Getter
    @Setter
    public static class Credentials {
        private String accessKey;
        private String secretKey;
    }

    @Getter
    @Setter
    public static class S3 {
        private String bucketName;
        private String endPoint;
        private String region;
    }
}
