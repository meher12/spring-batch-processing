package com.guru2batch.config;

import com.guru2batch.model.StudentCsv;
import com.guru2batch.model.StudentJdbc;
import com.guru2batch.model.StudentJson;
import com.guru2batch.model.StudentXML;
import com.guru2batch.writer.ItemWriterCsv;
import com.guru2batch.writer.ItemWriterJdbc;
import com.guru2batch.writer.ItemWriterJson;
import com.guru2batch.writer.ItemWriterXml;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.sql.DataSource;

@Configuration
public class SimpleJobWithoutProcessor {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    @Autowired
    private ItemWriterCsv itemWriterCsv;

    @Autowired
    private ItemWriterJson itemWriterJson;

    @Autowired
    private ItemWriterXml itemWriterXml;

    @Autowired
    private ItemWriterJdbc itemWriterJdbc;

    /*@Autowired
    private DataSource datasource;*/

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource datasource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.universitydatasource")
    public DataSource universitydatasource() {
        return DataSourceBuilder.create().build();
    }


    @Bean
    public Job chunkJob() {
        return jobBuilderFactory.get("Chunk Job")
                .incrementer(new RunIdIncrementer())
                .start(firstChunkStep())
                .build();
    }

    public Step firstChunkStep() {
        return stepBuilderFactory.get("First Chunk Step")
                .<StudentJdbc, StudentJdbc>chunk(3)
                //.reader(flatFileItemReader(null))
                //.reader(jsonItemReader(null))
                //.reader(staxEventItemReader(null))
                .reader(jdbcCursorItemReader())
                //.writer(itemWriterCsv)
                //.writer(itemWriterJson)
                //.writer(itemWriterXml)
                .writer(itemWriterJdbc)
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
                new File("/home/meher/j2eews/spring-batch-processing/2.0-spring-batch/src/main/resources//inputFiles/students.csv")
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

        // Customize Flat File Item Reader
        /*DefaultLineMapper<StudentCsv> defaultLineMapper =
                new DefaultLineMapper<StudentCsv>();

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames("ID", "First Name", "Last Name", "Email");

        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

        BeanWrapperFieldSetMapper<StudentCsv> fieldSetMapper =
                new BeanWrapperFieldSetMapper<StudentCsv>();
        fieldSetMapper.setTargetType(StudentCsv.class);

        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        flatFileItemReader.setLineMapper(defaultLineMapper);*/

        flatFileItemReader.setLinesToSkip(1);
        return flatFileItemReader;
    }

    @StepScope
    @Bean
    public JsonItemReader<StudentJson> jsonItemReader(
            @Value("#{jobParameters['inputFile']}") FileSystemResource fileSystemResource) {
        JsonItemReader<StudentJson> jsonItemReader =
                new JsonItemReader<StudentJson>();

        jsonItemReader.setResource(fileSystemResource);
        jsonItemReader.setJsonObjectReader(
                new JacksonJsonObjectReader<>(StudentJson.class));

        // To read just 8 itmes from json file
        jsonItemReader.setMaxItemCount(8);

        // start from item 3
        jsonItemReader.setCurrentItemCount(2);

        return jsonItemReader;
    }

    @StepScope
    @Bean
    public StaxEventItemReader<StudentXML> staxEventItemReader(
            @Value("#{jobParameters['inputFile']}") FileSystemResource fileSystemResource) {
        StaxEventItemReader<StudentXML> staxEventItemReader =
                new StaxEventItemReader<StudentXML>();

        staxEventItemReader.setResource(fileSystemResource);
        staxEventItemReader.setFragmentRootElementName("student");
        staxEventItemReader.setUnmarshaller(new Jaxb2Marshaller() {
            {
                setClassesToBeBound(StudentXML.class);
            }
        });

        return staxEventItemReader;
    }

    public JdbcCursorItemReader<StudentJdbc> jdbcCursorItemReader() {
        JdbcCursorItemReader<StudentJdbc> jdbcCursorItemReader =
                new JdbcCursorItemReader<StudentJdbc>();

        jdbcCursorItemReader.setDataSource(universitydatasource());
        jdbcCursorItemReader.setSql("SELECT id, first_name, last_name, email FROM student");

        jdbcCursorItemReader.setRowMapper(new BeanPropertyRowMapper<StudentJdbc>() {
            {
                setMappedClass(StudentJdbc.class);
            }
        });

        //jdbcCursorItemReader.setCurrentItemCount(2);
        //jdbcCursorItemReader.setMaxItemCount(8);

        return jdbcCursorItemReader;
    }
}

