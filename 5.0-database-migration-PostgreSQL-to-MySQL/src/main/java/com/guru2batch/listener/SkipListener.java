package com.guru2batch.listener;

import com.guru2batch.model.StudentCsv;
import com.guru2batch.model.StudentJson;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

@Component
public class SkipListener {

    @OnSkipInRead
    public void skipInRead(Throwable throwable){
        if(throwable instanceof FlatFileParseException) {
            createFile("/home/meher/j2eews/spring-batch-processing/4.0-fault-Tolerance-For-Spring-Batch-Job/Chunk Job/First Chunk Step/reader/SkipInRead.txt",
                    ((FlatFileParseException) throwable).getInput());
        }
    }

    @OnSkipInProcess
    public void skipInProcess(StudentCsv studentCsv, Throwable th) {
        createFile("/home/meher/j2eews/spring-batch-processing/4.0-fault-Tolerance-For-Spring-Batch-Job/Chunk Job/First Chunk Step/processor/SkipInProcess.txt",
                studentCsv.toString());
    }

    @OnSkipInWrite
    public void skipInWriter(StudentJson studentJson, Throwable th) {
        createFile("/home/meher/j2eews/spring-batch-processing/4.0-fault-Tolerance-For-Spring-Batch-Job/Chunk Job/First Chunk Step/writer/SkipInWrite.txt",
                studentJson.toString());
    }

    public void createFile(String filePath, String data) {
        try(FileWriter fileWriter = new FileWriter(new File(filePath), true)) {
            fileWriter.write(data + "," + new Date() + "\n");
        }catch(Exception e) {

        }
    }
}
