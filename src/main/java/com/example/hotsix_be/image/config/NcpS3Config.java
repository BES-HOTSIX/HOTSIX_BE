package com.example.hotsix_be.image.config;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class NcpS3Config {
    private final NcpS3Properties ncpS3Properties;

    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(
                ncpS3Properties.getCredentials().getAccessKey(),
                ncpS3Properties.getCredentials().getSecretKey());

        return (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                ncpS3Properties.getS3().getEndPoint(),
                                ncpS3Properties.getS3().getRegion()))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
