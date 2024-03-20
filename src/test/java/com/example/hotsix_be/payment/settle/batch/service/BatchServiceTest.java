package com.example.hotsix_be.payment.settle.batch.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/*
@SpringBootTest
@SpringBatchTest
@ActiveProfiles("test")
public class BatchServiceTest {
    @Autowired
    private JobLauncherTestUtils settleJobLauncherTestUtils;

    @DisplayName("settleJob launchTest")
    @Test
    public void t1() throws Exception {

        // given
        LocalDate currentDateTime = LocalDate.now();

        LocalDate endDay = currentDateTime
                .with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));

        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDate("endDay", endDay)
                .addJobParameters(settleJobLauncherTestUtils.getUniqueJobParameters())
                .toJobParameters();

        // when
        JobExecution jobExecution = settleJobLauncherTestUtils.launchJob(jobParameters);

        // then
        Assertions.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
        Assertions.assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
    }
}
 */
