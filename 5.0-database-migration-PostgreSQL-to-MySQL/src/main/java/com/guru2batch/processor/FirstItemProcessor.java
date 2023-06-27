package com.guru2batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.guru2batch.model.StudentCsv;
import com.guru2batch.model.StudentJson;

@Component
public class FirstItemProcessor implements ItemProcessor<StudentCsv, StudentJson> {

	@Override
	public StudentJson process(StudentCsv item) throws Exception {
		
		if(item.getId() == 6) {
			System.out.println("Inside Item Processor");
			throw new NullPointerException();
		}
		
		StudentJson studentJson = new StudentJson();
		studentJson.setId(item.getId());
		studentJson.setFirstName(item.getFirstName());
		studentJson.setLastName(item.getLastName());
		studentJson.setEmail(item.getEmail());
		
		return studentJson;
	}

}
