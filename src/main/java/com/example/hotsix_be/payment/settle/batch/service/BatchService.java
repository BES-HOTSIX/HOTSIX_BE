package com.example.hotsix_be.payment.settle.batch.service;

<<<<<<< HEAD
import com.example.hotsix_be.common.utils.Ut;
=======
>>>>>>> 55fe32653994a3780e5fb7afea5eab11fabbddae
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
import java.time.LocalDate;
=======
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
>>>>>>> 55fe32653994a3780e5fb7afea5eab11fabbddae

@Service
@RequiredArgsConstructor
public class BatchService {
    private final JobLauncher jobLauncher;
    private final Job settleJob;

    @SneakyThrows
    @Scheduled(cron = "0 0-30/10 0 ? * WED") // 매주 수요일 0시 0분에 실행, 10분마다 한번씩, 총 네번 실행
<<<<<<< HEAD
    public void runSettleJob() {

        LocalDate endDay = Ut.getSettleDate();
=======
//    @Scheduled(cron = "0/3 * * ? * *") // 테스트용 스케줄러 어노테이션 (3초마다 한번씩 실행)
    public void runSettleJob() {
        LocalDate currentDateTime = LocalDate.now();

        LocalDate endDay = currentDateTime
                .with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
>>>>>>> 55fe32653994a3780e5fb7afea5eab11fabbddae

        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDate("endDay", endDay)
                .toJobParameters();

        jobLauncher.run(settleJob, jobParameters);

        System.out.println("################## runSettleJob is applied ##################");
    }
}
