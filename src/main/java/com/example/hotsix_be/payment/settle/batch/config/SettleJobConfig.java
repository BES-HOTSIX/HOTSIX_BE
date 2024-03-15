package com.example.hotsix_be.payment.settle.batch.config;

import com.example.hotsix_be.payment.settle.entity.Settle;
import com.example.hotsix_be.payment.settle.service.SettleService;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class SettleJobConfig {
    private final ReservationRepository reservationRepository;
    private final SettleService settleService;

    private final int CHUNK_SIZE = 20;

    @Bean
    public Job settleJob(JobRepository jobRepository, Step settleStep) {
        return new JobBuilder("settleJob", jobRepository)
                .start(settleStep)
                .build();
    }

    @JobScope
    @Bean
    public Step settleStep(
            JobRepository jobRepository,
            ItemReader<Reservation> settleItemReader,
            ItemProcessor<Reservation, Settle> settleItemProcessor,
            ItemWriter<Settle> settleItemWriter,
            PlatformTransactionManager platformTransactionManager
    ) {
        return new StepBuilder("settleStep", jobRepository)
                .<Reservation, Settle>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(settleItemReader)
                .processor(settleItemProcessor)
                .writer(settleItemWriter)
                .build();
    }

    @StepScope
    @Bean
    public ItemReader<Reservation> settelItemReader(
            @Value("#{jobParameters['endDay']}") LocalDate endDay
    ) {
        return new RepositoryItemReaderBuilder<Reservation>()
                .name("settleStep")
                .repository(reservationRepository)
                .methodName("findBySettleDateNullAndCheckOutDateLessThanEqualAndCancelDateNull")
                .pageSize(CHUNK_SIZE)
                .arguments(endDay)
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Reservation, Settle> settleItemProcessor() {
        return settleService::doSettle;
    }

    @StepScope
    @Bean
    public ItemWriter<Settle> settleItemWriter() {
        return settles -> settles.forEach(settleService::save);
    }
}
