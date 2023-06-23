package com.guru2batch.writer;

import com.guru2batch.model.StudentJson;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemWriterJson implements ItemWriter<StudentJson> {


    @Override
    public void write(List<? extends StudentJson> studentList) throws Exception {
        System.out.println("Inside Item Writer");
        studentList.stream().forEach(System.out::println);

    }
}
