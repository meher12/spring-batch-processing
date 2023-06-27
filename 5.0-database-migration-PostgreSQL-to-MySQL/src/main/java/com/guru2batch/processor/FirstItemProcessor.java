package com.guru2batch.processor;

import com.guru2batch.postgresentity.Student;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.guru2batch.model.StudentCsv;
import com.guru2batch.model.StudentJson;

@Component
public class FirstItemProcessor implements ItemProcessor<Student, com.guru2batch.mysqlentity.Student> {

	@Override
	public com.guru2batch.mysqlentity.Student process(Student item) throws Exception {
		System.out.println(item.getId());

		com.guru2batch.mysqlentity.Student student = new
				com.guru2batch.mysqlentity.Student();

		student.setId(item.getId());
		student.setFirstName(item.getFirstName());
		student.setLastName(item.getLastName());
		student.setEmail(item.getEmail());
		student.setDeptId(item.getDeptId());
		student.setIsActive(item.getIsActive() != null ?
				Boolean.valueOf(item.getIsActive()) : false);
		return student;
	}
}
