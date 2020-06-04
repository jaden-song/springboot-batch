package com.example.batchprocessing.test1Batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SleepTasklet implements Tasklet, StepExecutionListener {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("SLEEP 10 seconds !!!!");
        TimeUnit.SECONDS.sleep(10);
        return RepeatStatus.FINISHED;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("------ Here is BeforeStep ------ : " + sdf.format(new Date()));
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("------ Here is AfterStep ------ : " + sdf.format(new Date()));
        return ExitStatus.COMPLETED;
    }
}
