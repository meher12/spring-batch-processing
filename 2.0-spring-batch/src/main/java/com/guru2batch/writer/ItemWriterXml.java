package com.guru2batch.writer;

import com.guru2batch.model.StudentXML;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemWriterXml implements ItemWriter<StudentXML> {

    @Override
    public void write(List<? extends StudentXML> list) throws Exception {

        System.out.println("Inside Item Writer");
        list.stream().forEach(System.out::println);

    }
}
