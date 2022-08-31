package com.example.springbatch.part1;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configurable
@Slf4j
@RequiredArgsConstructor
public class HelloConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job helloJob(){
        return jobBuilderFactory.get("helloJob")
                    .incrementer(new RunIdIncrementer())    // 파라미터 id를 자동으로 생성해주는 객체
                    .start(this.hellStep())                 // 최초로 실행될 메서드
                    .build();
    }

    @Bean
    public Step hellStep() {
        return stepBuilderFactory.get("helloStep") 
                    .tasklet((contribution, ChunkContext) -> {
                        log.info("hello spring batch");
                        return RepeatStatus.FINISHED;
                    }).build();
    }
}
