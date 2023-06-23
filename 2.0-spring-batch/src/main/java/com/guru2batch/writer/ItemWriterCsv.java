package com.guru2batch.writer;

import com.guru2batch.model.StudentCsv;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemWriterCsv implements ItemWriter<StudentCsv> {
    @Override
    public void write(List<? extends StudentCsv> studentList) throws Exception {
        System.out.println("Inside Item Writer");
        studentList.stream().forEach(System.out::println);
    }
}
