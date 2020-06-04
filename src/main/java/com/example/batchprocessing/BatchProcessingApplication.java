package com.example.batchprocessing;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//@SpringBootApplication(scanBasePackages = "com.example.batchprocessing.test2Scheduler")
@SpringBootApplication
@EnableScheduling
@EnableBatchProcessing
@EnableSchedulerLock(defaultLockAtMostFor = "1m")
public class BatchProcessingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchProcessingApplication.class, args);
    }

}
