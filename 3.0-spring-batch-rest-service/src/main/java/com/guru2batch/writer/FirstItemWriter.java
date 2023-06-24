package com.guru2batch.writer;

import java.util.List;

import com.guru2batch.model.StudentResponse;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class FirstItemWriter implements ItemWriter<StudentResponse> {

	@Override
	public void write(List<? extends StudentResponse> items) throws Exception {
		System.out.println("Inside Item Writer");
		items.stream().forEach(System.out::println);
	}

}
