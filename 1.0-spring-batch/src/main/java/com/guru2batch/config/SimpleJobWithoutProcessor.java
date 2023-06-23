package com.guru2batch.config;

import com.guru2batch.listener.FirstJoblistener;
import com.guru2batch.listener.FirstStepListener;
import com.guru2batch.processor.FirstItemProcessor;
import com.guru2batch.reader.FirstItemReader;
import com.guru2batch.service.SecondTasklet;
import com.guru2batch.writer.FirstItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimpleJobWithoutProcessor {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private SecondTasklet secondTasklet;

    @Autowired
    private FirstJoblistener firstJoblistener;

    @Autowired
    private FirstStepListener firstStepListener;

    @Autowired
    private FirstItemReader firstItemReader;

    @Autowired
    private FirstItemProcessor firstItemProcessor;

    @Autowired
    private FirstItemWriter firstItemWriter;

    @Bean
    public Job firstJob() {
        return jobBuilderFactory.get("First Job")
                .incrementer(new RunIdIncrementer())
                .start(firstStep())
                .next(secondStep())
                .listener(firstJoblistener)
                .build();
    }

    private Step firstStep() {
        return stepBuilderFactory.get("First Step")
                .tasklet(firstTask())
                .listener(firstStepListener)
                .build();
    }

    private Tasklet firstTask() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                System.out.println("This is first tasklet step");
                System.out.println("SEC = " + chunkContext.getStepContext().getJobExecutionContext());
                return RepeatStatus.FINISHED;
            }
        };
    }

    private Step secondStep() {
        return stepBuilderFactory.get("Second Step")
                .tasklet(secondTasklet)
                .build();
    }


    @Bean
    public Job secondJob() {
        return jobBuilderFactory.get("Second Job")
                .incrementer(new RunIdIncrementer())
                .start(firstChunkStep())
                //.next(secondStep())
                .build();
    }

    public Step firstChunkStep(){
        return stepBuilderFactory.get("First Chunk Step")
                // I/O: <Integer, Long>
                .<Integer, Long>chunk(4)
                .reader(firstItemReader)
                //.processor(firstItemProcessor)
                .writer(firstItemWriter)
                .build();
    }
}

