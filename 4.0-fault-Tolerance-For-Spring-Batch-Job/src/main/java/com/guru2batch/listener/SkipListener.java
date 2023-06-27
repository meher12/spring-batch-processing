package com.guru2batch.listener;

import org.springframework.batch.core.annotation.OnSkipInRead;
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

    public void createFile(String filePath, String data) {
        try(FileWriter fileWriter = new FileWriter(new File(filePath), true)) {
            fileWriter.write(data + "," + new Date() + "\n");
        }catch(Exception e) {

        }
    }
}
