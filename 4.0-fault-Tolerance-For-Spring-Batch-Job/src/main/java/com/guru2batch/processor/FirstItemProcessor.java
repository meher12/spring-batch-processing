package com.guru2batch.processor;

import com.guru2batch.model.StudentCsv;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.guru2batch.model.StudentJson;

@Component
public class FirstItemProcessor implements ItemProcessor<StudentCsv, StudentJson> {

	@Override
	public StudentJson process(StudentCsv item) throws Exception {
		System.out.println("Inside Item Processor");
		StudentJson studentJson = new StudentJson();
		studentJson.setId(item.getId());
		studentJson.setFirstName(item.getFirstName());
		studentJson.setLastName(item.getLastName());
		studentJson.setEmail(item.getEmail());
		
		return studentJson;
	}

}
