package com.guru2batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FirstItemProcessor implements ItemProcessor<Integer, Long> {


    // Integer: Input data
    // Long: Output data
    @Override
    public Long process(Integer item) throws Exception {
        System.out.println("Inside Item Processor");
        return Long.valueOf(item + 20);
    }
}
