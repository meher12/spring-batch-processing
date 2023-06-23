package com.guru2batch.controller;

import com.guru2batch.request.JobParamsRequest;
import com.guru2batch.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job")
public class JobController {
    @Autowired
    private JobService jobService;

    @GetMapping("/start/{jobName}")
    public String startJob(@PathVariable String jobName,
                           @RequestBody List<JobParamsRequest> jobParamsRequestsList) throws Exception {
        jobService.startJob(jobName, jobParamsRequestsList);
        return "Job " +  jobName + " Started..." ;
    }
}
