package com.example.batchprocessing.test2Scheduler;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SchedulerBatch {
    private final JobLauncher jobLauncher;
    private final Job job;

    public SchedulerBatch(JobLauncher jobLauncher, @Qualifier("sleepJob") Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @Scheduled(cron = "0/5 * * * * *")
    @SchedulerLock(name = "ShedLock_Test", lockAtLeastFor = "10s", lockAtMostFor = "10s")
    public void callBatchJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        log.info("Job finished with status :" + jobExecution.getStatus());
    }
}
