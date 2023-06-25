package com.guru2batch.config;

import com.guru2batch.model.StudentCsv;
import com.guru2batch.model.StudentJdbc;
import com.guru2batch.model.StudentJson;
import com.guru2batch.model.StudentXML;
import com.guru2batch.processor.ItemProcessorJdbcToJson;
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
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
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
import java.io.IOException;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

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

    @Autowired
    private ItemProcessorJdbcToJson itemProcessorJdbcToJson;

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
                .<StudentCsv, StudentCsv>chunk(3)
                .reader(flatFileItemReader(null))
                //.reader(jsonItemReader(null))
                //.reader(staxEventItemReader(null))
                //.reader(jdbcCursorItemReader())
                //.writer(itemWriterCsv)
                //.writer(itemWriterJson)
                //.writer(itemWriterXml)
                //.writer(itemWriterJdbc)
                //.writer(flatFileItemWriter(null))
                //.processor(itemProcessorJdbcToJson)
                //.writer(jsonFileItemWriter(null))
                //.writer(jsonFileItemWriterWithProcessor(null))
                //.writer(staxEventItemWriter(null))
                //.writer(jdbcBatchItemWriter())
                .writer(jdbcBatchItemWriter1())
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
                new File("/home/meher/j2eews/spring-batch-processing/2.0-spring-batch/src/main/resources/inputFiles/students.csv")
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

    //Create Flat File Item Writer with CSV File
    @StepScope
    @Bean
    public FlatFileItemWriter<StudentJdbc> flatFileItemWriter(
            //outputFile=/home/meher/j2eews/spring-batch-processing/2.0-spring-batch/src/main/resources/outputFiles/students.csv
            @Value("#{jobParameters['outputFile']}") FileSystemResource fileSystemResource) {
        FlatFileItemWriter<StudentJdbc> flatFileItemWriter =
                new FlatFileItemWriter<StudentJdbc>();

        flatFileItemWriter.setResource(fileSystemResource);

        flatFileItemWriter.setHeaderCallback(new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                writer.write("Id,First Name,Last Name,Email");
                //writer.write("Id|First Name|Last Name|Email");
            }
        });

        flatFileItemWriter.setLineAggregator(new DelimitedLineAggregator<StudentJdbc>() {
            {
                //setDelimiter("|");
                setFieldExtractor(new BeanWrapperFieldExtractor<StudentJdbc>() {
                    {
                        setNames(new String[]{"id", "firstName", "lastName", "email"});
                    }
                });
            }
        });

        flatFileItemWriter.setFooterCallback(new FlatFileFooterCallback() {
            @Override
            public void writeFooter(Writer writer) throws IOException {
                writer.write("Created @ " + new Date());
            }
        });

        return flatFileItemWriter;
    }

    //JSON Item Writer
    @StepScope
    @Bean
    public JsonFileItemWriter<StudentJdbc> jsonFileItemWriter(
            @Value("#{jobParameters['outputFile']}") FileSystemResource fileSystemResource) {
        JsonFileItemWriter<StudentJdbc> jsonFileItemWriter =
                new JsonFileItemWriter<>(fileSystemResource,
                        new JacksonJsonObjectMarshaller<StudentJdbc>());

        return jsonFileItemWriter;
    }

    //JSON Item Writer by Processor
    @StepScope
    @Bean
    public JsonFileItemWriter<StudentJson> jsonFileItemWriterWithProcessor(
            @Value("#{jobParameters['outputFile']}") FileSystemResource fileSystemResource) {
        JsonFileItemWriter<StudentJson> jsonFileItemWriter =
                new JsonFileItemWriter<>(fileSystemResource,
                        new JacksonJsonObjectMarshaller<StudentJson>());

        return jsonFileItemWriter;
    }

    // XML Item Writer
    @StepScope
    @Bean
    public StaxEventItemWriter<StudentJdbc> staxEventItemWriter(
            @Value("#{jobParameters['outputFile']}") FileSystemResource fileSystemResource) {
        StaxEventItemWriter<StudentJdbc> staxEventItemWriter =
                new StaxEventItemWriter<StudentJdbc>();

        staxEventItemWriter.setResource(fileSystemResource);
        staxEventItemWriter.setRootTagName("students");

        staxEventItemWriter.setMarshaller(new Jaxb2Marshaller() {
            {
                setClassesToBeBound(StudentJdbc.class);
            }
        });

        return staxEventItemWriter;
    }

    //CSV with Item Reader to Mysql DB with Item Writer
    @Bean
    public JdbcBatchItemWriter<StudentCsv> jdbcBatchItemWriter() {
        JdbcBatchItemWriter<StudentCsv> jdbcBatchItemWriter =
                new JdbcBatchItemWriter<StudentCsv>();

        jdbcBatchItemWriter.setDataSource(universitydatasource());
        jdbcBatchItemWriter.setSql(
                "insert into student(id, first_name, last_name, email) "
                        + "values (:id, :firstName, :lastName, :email)");

        jdbcBatchItemWriter.setItemSqlParameterSourceProvider(
                new BeanPropertyItemSqlParameterSourceProvider<StudentCsv>());

        return jdbcBatchItemWriter;
    }


    //JDBC Item Writer Using Prepared Statement
    @Bean
    public JdbcBatchItemWriter<StudentCsv> jdbcBatchItemWriter1() {
        JdbcBatchItemWriter<StudentCsv> jdbcBatchItemWriter =
                new JdbcBatchItemWriter<StudentCsv>();

        jdbcBatchItemWriter.setDataSource(universitydatasource());
        jdbcBatchItemWriter.setSql(
                "insert into student(id, first_name, last_name, email) "
                        + "values (?,?,?,?)");
        jdbcBatchItemWriter.setItemPreparedStatementSetter(
                new ItemPreparedStatementSetter<StudentCsv>() {
                    @Override
                    public void setValues(StudentCsv studentCsv, PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setLong(1, studentCsv.getId());
                        preparedStatement.setString(2, studentCsv.getFirstName());
                        preparedStatement.setString(3, studentCsv.getLastName());
                        preparedStatement.setString(4, studentCsv.getEmail());
                    }
                }
        );


        return jdbcBatchItemWriter;
    }
}

