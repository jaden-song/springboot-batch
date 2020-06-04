package com.example.batchprocessing.test2Scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    private final int POOL_SIZE = 10;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 수행시간과 스케줄 시간이 중복이 되더라도 중복 실행하진 않는다
//    @Scheduled(cron = "0/2 * * * * *")  // 수행이 마친 기준으로 2초마다 실행, 앞뒤로 1초의 간격을 두고 실행
//    @Scheduled(fixedRate = 2000)  // 시작 기준으로 2초마다 실행, 앞뒤로 0초의 간격을 두고 실행
    public void cronScheduler() throws InterruptedException {
        String start = sdf.format(new Date());
        TimeUnit.SECONDS.sleep(5);
        log.info("---------------------------------------------------------------------------------------------------");
        log.info("Java cron job expression:: " + start + " - " +sdf.format(new Date()));
    }

//    @Scheduled(cron = "0/2 * * * * *")
    public void cronScheduler2() throws InterruptedException {
        String start = sdf.format(new Date());
        TimeUnit.SECONDS.sleep(5);
        log.info("---------------------------------------------------------------------------------------------------");
        log.info("Java cron job2 expression:: " + start + " - " +sdf.format(new Date()));
    }

//    @Scheduled(cron = "0/5 * * * * *")
    public void cronScheduler3() throws InterruptedException {
        log.info("---------------------------------------------------------------------------------------------------");
        log.info("Java cron job3 expression:: " + sdf.format(new Date()));
    }

    // cronScheduler1,2 는 동시수행으로 설정하지만 하나의 쓰레드이기에 한번씩 순서대로 실행됨
    // 아래처럼 ThreadPool 로 스케줄러를 설정하면 동시에 수행된다.
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
        threadPoolTaskScheduler.setThreadNamePrefix("POOL-");
        threadPoolTaskScheduler.initialize();
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
}
