package com.guru2batch.processor;

import com.guru2batch.model.StudentJdbc;
import com.guru2batch.model.StudentJson;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ItemProcessorJdbcToJson implements ItemProcessor<StudentJdbc, StudentJson> {
    @Override
    public StudentJson process(StudentJdbc studentJdbc) throws Exception {
        System.out.println("Inside Item Processor");
        StudentJson studentJson = new StudentJson();
        studentJson.setId(studentJdbc.getId());
        studentJson.setFirstName(studentJdbc.getFirstName());
        studentJson.setLastName(studentJdbc.getLastName());
        studentJson.setEmail(studentJdbc.getEmail());

        return studentJson;

    }
}
