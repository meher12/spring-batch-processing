package com.guru2batch.reader;

import com.guru2batch.model.StudentCsv;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

@Component
public class ItemReaderCSV implements ItemReader<StudentCsv> {

    @Override
    public StudentCsv read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }
}
