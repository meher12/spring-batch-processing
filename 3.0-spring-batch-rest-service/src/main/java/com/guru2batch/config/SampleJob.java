package com.guru2batch.config;

import com.guru2batch.model.StudentCsv;
import com.guru2batch.model.StudentResponse;
import com.guru2batch.reader.FirstItemReader;
import com.guru2batch.service.StudentService;
import com.guru2batch.writer.FirstItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;


@Configuration
public class SampleJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private FirstItemReader firstItemReader;


    @Autowired
    private FirstItemWriter firstItemWriter;

    @Autowired
    private StudentService studentService;


    @Bean
    public Job chunkJob() {
        return jobBuilderFactory.get("Chunk Job")
                .incrementer(new RunIdIncrementer())
                .start(firstChunkStep())
                .build();
    }

    private Step firstChunkStep() {
        return stepBuilderFactory.get("First Chunk Step")
                //.<StudentResponse, StudentResponse>chunk(3)
                .<StudentCsv, StudentCsv>chunk(3)
                //.reader(itemReaderAdapter())
                //.writer(firstItemWriter)
                .reader(flatFileItemReader(null))
                .writer(itemWriterAdapter())
                .build();
    }


    @StepScope
    @Bean
    public FlatFileItemReader<StudentCsv> flatFileItemReader(
            @Value("#{jobParameters['inputFile']}") FileSystemResource fileSystemResource) {
        FlatFileItemReader<StudentCsv> flatFileItemReader =
                new FlatFileItemReader<StudentCsv>();
       /*  inputFile=path
         flatFileItemReader.setResource(new FileSystemResource(
                new File("/home/meher/j2eews/spring-batch-processing/3.0-spring-batch-rest-service/src/main/resources/students.csv")
        ));*/
        flatFileItemReader.setResource(fileSystemResource);
        flatFileItemReader.setLineMapper(new DefaultLineMapper<StudentCsv>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames("ID", "First Name", "Last Name", "Email");
                        //setDelimiter("|");
                    }
                });

                setFieldSetMapper(new BeanWrapperFieldSetMapper<StudentCsv>() {
                    {
                        setTargetType(StudentCsv.class);
                    }
                });

            }
        });


        flatFileItemReader.setLinesToSkip(1);
        return flatFileItemReader;
    }
    public ItemReaderAdapter<StudentResponse> itemReaderAdapter() {
        ItemReaderAdapter<StudentResponse> itemReaderAdapter =
                new ItemReaderAdapter<StudentResponse>();

        itemReaderAdapter.setTargetObject(studentService);
        itemReaderAdapter.setTargetMethod("getStudent");
        itemReaderAdapter.setArguments(new Object[] {1L, "Test"});

        return itemReaderAdapter;
    }

    public ItemWriterAdapter<StudentCsv> itemWriterAdapter() {
        ItemWriterAdapter<StudentCsv> itemWriterAdapter =
                new ItemWriterAdapter<StudentCsv>();

        itemWriterAdapter.setTargetObject(studentService);
        itemWriterAdapter.setTargetMethod("restCallToCreateStudent");

        return itemWriterAdapter;
    }
}
