package com.guru2batch.config;

import com.guru2batch.listener.SkipListener;
import com.guru2batch.listener.SkipListenerImpl;
import com.guru2batch.model.StudentCsv;
import com.guru2batch.model.StudentJson;
import com.guru2batch.postgresentity.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class SampleJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    @Autowired
    private SkipListener skipListener;

    @Autowired
    private SkipListenerImpl skipListenerImpl;

    @Autowired
    @Qualifier("datasource")
    private DataSource datasource;

    // Jdbc Item Writer (Mysql DB)
    @Autowired
    @Qualifier("universitydatasource")
    private DataSource universitydatasource;

    // Jdbc Item Reader (postgresSql DB)
    @Autowired
    @Qualifier("postgresdatasource")
    private DataSource postgresdatasource;


    // Jdbc Item Reader (postgresSql DB)
    @Autowired
    @Qualifier("postgresqlEntityManagerFactory")
    private EntityManagerFactory postgresqlEntityManagerFactory;

    //// Jdbc Item Writer (Mysql DB)
    @Autowired
    @Qualifier("mysqlEntityManagerFactory")
    private EntityManagerFactory mysqlEntityManagerFactory;


    @Bean
    public Job chunkJob() {
        return jobBuilderFactory.get("Chunk Job")
                .incrementer(new RunIdIncrementer())
                .start(firstChunkStep())
                .build();
    }

    private Step firstChunkStep() {
        return stepBuilderFactory.get("First Chunk Step")
                .<Student, com.guru2batch.mysqlentity.Student>chunk(3)
                .reader(jpaCursorItemReader())
                //.processor(firstItemProcessor)
                //.writer(jsonFileItemWriter(null))
                .faultTolerant()
                .skip(Throwable.class)
                //.skip(NullPointerException.class)
                .skipLimit(100)
                //.skipPolicy(new AlwaysSkipItemSkipPolicy())
                .retryLimit(3)
                .retry(Throwable.class)
                //.listener(skipListener)
                .listener(skipListenerImpl)
                .build();
    }

    // jpa postgresql item reader
    public JpaCursorItemReader<Student> jpaCursorItemReader() {
        JpaCursorItemReader<Student> jpaCursorItemReader =
                new JpaCursorItemReader<Student>();

        jpaCursorItemReader.setEntityManagerFactory(postgresqlEntityManagerFactory);

        jpaCursorItemReader.setQueryString("From Student");

        return jpaCursorItemReader;
    }

}
