package com.guru2batch.writer;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class FirstItemWriter implements ItemWriter<Long> {

    @Override
    public void write(List<? extends Long> items) throws Exception {
        System.out.println("Inside Item Writer");
        items.stream().forEach(System.out::println);
    }
}
