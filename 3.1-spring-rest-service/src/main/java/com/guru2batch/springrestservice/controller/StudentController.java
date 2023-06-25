package com.guru2batch.springrestservice.controller;

import com.guru2batch.springrestservice.request.StudentRequest;
import com.guru2batch.springrestservice.response.StudentResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class StudentController {

    @GetMapping("/students")
    public List<StudentResponse> students() {
        return Arrays.asList(
                new StudentResponse(1L, "John", "Smith", "john@gmail.com"),
                new StudentResponse(2L, "Sachin", "Dave", "sachin@gmail.com"),
                new StudentResponse(3L, "Peter", "Mark", "peter@gmail.com"),
                new StudentResponse(4L, "Martin", "Shirk", "martin@gmail.com"),
                new StudentResponse(5L, "Mona", "Sharma", "mona@gmail.com")
        );
    }

    @PostMapping("/createStudent")
    public StudentResponse createStudent(@RequestBody StudentRequest studentRequest) {
        System.out.println("Student Created " + studentRequest.getId());
        return new StudentResponse(studentRequest.getId(),
                studentRequest.getFirstName(),
                studentRequest.getLastName(),
                studentRequest.getEmail());
    }
}
