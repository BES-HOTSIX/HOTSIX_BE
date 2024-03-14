package com.example.hotsix_be.payment.settle.batch.service;

import com.example.hotsix_be.payment.settle.utils.SettleUt;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BatchService {
    private final JobLauncher jobLauncher;
    private final Job settleJob;

    @SneakyThrows
    @Scheduled(cron = "0 0-30/10 0 ? * WED") // 매주 수요일 0시 0분에 실행, 10분마다 한번씩, 총 네번 실행
    public void runSettleJob() {

        LocalDate endDay = SettleUt.getSettleDate();

        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDate("endDay", endDay)
                .toJobParameters();

        jobLauncher.run(settleJob, jobParameters);

        System.out.println("################## runSettleJob is applied ##################");
    }
}
