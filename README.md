# 스프링 부트 배치 테스트
스프링 부트에서 사용하는 Batch 와 Scheduler 에 대해서 하나씩 테스트해본다. 기본적으로 DB 는 mysql 과 편의상 lombok 을 사용한다.

## Gradle 설정
* spring-boot-starter-batch -> 배치를 사용
* spring-boot-starter-data-jpa -> database 사용
* mysql-connector-java -> mysql driver
* lombok -> 편리한 롬복
* shedlock-spring, shedlock-provider-jdbc-template -> Batch Cluster 를 위한 설정 Test3 에서 사

## Resources 설정
* application.properties
    * spring.batch.job.enabled=true -> Job 으로 등록된 Bean 은 자동 실행
    * spring.datasource~ -> mysql에 대한 Datasource Bean 이 자동 등록
    * spring.datasource.initialization-mode=always -> 테스트환경에서 서버기동시 테이블을 Drop, Create 처리
    * spring.datasource.platform=mysql -> 리소스파일중 platform 이름이 붙은 파일 실행
    * spring.datasource.sql-script-encoding=UTF-8 -> 한글 처리를 위함
* sample.csv -> 간단한 name 을 가진 파일
* schema-mysql.sql -> 스프링 기능으로 자동으로 호출되어 실행됨 (보통 DDL)
* data-mysql.sql -> 스프링 기능으로 자동으로 호출되어 실행 (보통 DML)

## 메인 Application 설정
* @EnableBatchProcessing -> 스프링 배치에 대한 사용 Test1
* @EnableScheduling -> 스프링 스케줄에 대한 사용 Test2
* @EnableSchedulerLock(defaultLockAtMostFor = "1m") -> cluster 처리에 대한 사용 Test3

## 참고 사이트 
* Spring : https://docs.spring.io/spring-batch/docs/4.2.x/reference/html/index.html
* ShedLock : https://github.com/lukas-krecan/ShedLock
* Spring Scheduling : https://spring.io/guides/gs/scheduling-tasks/
* Spring Batch : https://spring.io/guides/gs/batch-processing/

## Test1 Batch
Job은 Step으로 구성되, Step은 단순처리가능한 Tasklet 이나 chunck 구조의 reader - processor - writer 구조로 만들 수 있다.  
각 Job, Step 은 Listener 를 통해 전후를 인터셉트 할 수 있다.

* personJob -> sample.csv 파일을 Read - 이름 대문자 변환 Process - DB 에 Write 하는 Job
* sleepJob -> 간단히 10초를 대기하는 Job

## Test2 Scheduler
@Scheduler 어노테이션을 통해 Cron 또는 Fixed~ 를 통해 설정 가능하다

* SchedulerConfig.java
    * 몇가지 예제가 있으며, 특히 동일 시간의 스케줄일경우 동시 실행이 안되기에 SchedulingConfigurer 의 configureTasks 재정의를 하면 가능
* SchedulerBatch.java
    * Scheduler 를 통해 Test1의 sleepJob을 실행해 본다.
    * 어노테이션에 의해 Job 을 주입받으면 동일한 Bean 이 2개 (personJob, sleepJob) 발생하기에 @Qualifier 를 통해 구분해준다.

## Test3 ShedLock
두개의 Scheduler 어플리케이션이 Cluster 환경으로 실행될 경우 중복 실행을 막기 위한 방법이다  

먼저 테이블을 만들어야 하는데 이미 schema-mysql.sql 에 존재한다.
> CREATE TABLE shedlock(
      name VARCHAR(64) NOT NULL,
      lock_until TIMESTAMP(3) NOT NULL,
      locked_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
      locked_by VARCHAR(255) NOT NULL,
      PRIMARY KEY (name)
  )

메인 Application 의 어노테이션과 ShedLockConfig.java 의 lockProvider 를 Bean 으로 등록해준다.  
SchedulerBatch.java 에서 아래처럼 어노테이션을 선언하면 10초 동안 해당 스케줄은 Lock이 잡히게 된다.  
> @SchedulerLock(name = "ShedLock_Test", lockAtLeastFor = "10s", lockAtMostFor = "10s")

상세한건 참고사이트에서 확인하면 되는데... 실행해보니 잘 안된다.  

아마도 서버가 실행될때 마다 테이블을 초기화해서 그렇지 않을까 싶은데 다시 테스트 후 업뎃하는걸로...



