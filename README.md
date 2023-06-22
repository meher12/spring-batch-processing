# Batch processing with Spring Boot and Spring Batch
## 1. Getting Started with Spring Batch:
1. Create First Spring Batch Application
2. Job with First Tasklet Step
3. Job with Second Tasklet Step
4. Customize Tasklet Step
5. Job Instance, Job Execution & Job Execution Context: 
   1. Job Instance, Job Execution:  
          <div align="center">
                   <img src="jobexecution.jpg" width="400px"></img> 
          </div>
   2. Job Execution Context: context is at job level
          <div align="center">
                  <img src="jobcontext.jpg" width="400px"></img> 
          </div>
6. Step Execution & Step Execution Context:
           <div align="center">
              <img src="stepexecution.jpg" width="200px"></img> 
            </div>
7. Running Spring Batch with MySQL
8. Setting Job Parameter Value: 
   * In program argument put "run=one", "run=two" "a=b" to show in DB table name "BATCH_JOB_INSTANCE" another instance
9. Make Job Parameter Unique: with unique id we resolve pb of parameter already exist 
10. Job Listener
11. Step Listener
## 2. Chunk Oriented Step
1. Chunk Oriented Step:
        <div align="center">
            <img src="chunkOrientedStep.jpg" width="400px"></img> 
        </div>
   1. Create First Item Reader
   2. Create First Item Processor: has two parameter (input/output)
   3. Create First Item Writer


