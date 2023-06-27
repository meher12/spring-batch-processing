package com.guru2batch.listener;

import com.guru2batch.model.StudentCsv;
import com.guru2batch.model.StudentJson;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

@Component
public class SkipListenerImpl implements SkipListener<StudentCsv, StudentJson> {


    @Override
    public void onSkipInRead(Throwable throwable) {
        createFile("/home/meher/j2eews/spring-batch-processing/4.0-fault-Tolerance-For-Spring-Batch-Job/Chunk Job/First Chunk Step/reader/SkipInRead.txt",
                ((FlatFileParseException) throwable).getInput());
    }

    @Override
    public void onSkipInWrite(StudentJson studentJson, Throwable throwable) {
        createFile("/home/meher/j2eews/spring-batch-processing/4.0-fault-Tolerance-For-Spring-Batch-Job/Chunk Job/First Chunk Step/writer/SkipInWrite.txt",
                studentJson.toString());
    }

    @Override
    public void onSkipInProcess(StudentCsv studentCsv, Throwable throwable) {
        createFile("/home/meher/j2eews/spring-batch-processing/4.0-fault-Tolerance-For-Spring-Batch-Job/Chunk Job/First Chunk Step/processor/SkipInProcess.txt",
                studentCsv.toString());

    }

    public void createFile(String filePath, String data) {
        try (FileWriter fileWriter = new FileWriter(new File(filePath), true)) {
            fileWriter.write(data + "," + new Date() + "\n");
        } catch (Exception e) {

        }
    }
}
