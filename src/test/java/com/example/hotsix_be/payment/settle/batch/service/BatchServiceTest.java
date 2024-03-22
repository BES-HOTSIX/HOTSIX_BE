package com.example.hotsix_be.payment.settle.batch.service;


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
    }}
=======
    }
}
 */