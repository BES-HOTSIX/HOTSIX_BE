package com.example.hotsix_be.payment.settle.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BatchTestConfig {
    @Bean
    public JobLauncherTestUtils settleJobLauncherTestUtils(Job settleJob) {
        JobLauncherTestUtils utils = new JobLauncherTestUtils();
        utils.setJob(settleJob);
        return utils;
    }
}
