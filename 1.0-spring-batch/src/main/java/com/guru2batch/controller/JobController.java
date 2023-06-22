package com.guru2batch.controller;

import com.guru2batch.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/job")
public class JobController {
    @Autowired
    private JobService jobService;

    @GetMapping("/start/{jobName}")
    public String startJob(@PathVariable String jobName) throws Exception {
        jobService.startJob(jobName);
        return "Job " +  jobName + " Started..." ;
    }
}
