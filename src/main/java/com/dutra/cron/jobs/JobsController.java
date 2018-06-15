package com.dutra.cron.jobs;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Collection;
import java.util.Set;

import static com.dutra.cron.jobs.JobsController.JobCreationResponse.createFor;
import static com.dutra.cron.jobs.JobsController.JobCreationResponse.jobAlreadyExists;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.*;

@RestController("/api/jobs")
public class JobsController {

    @Autowired
    private Validator validator;
    @Autowired
    private JobService jobService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> scheduleJob(@RequestBody Job job) {

        Set<ConstraintViolation<Job>> violations = validator.validate(job);
        if (!violations.isEmpty()) {
            return badRequest()
                    .body(violations.stream().map(v -> v.getMessage()).collect(toList()));
        }

        return jobService.accept(job).map(j -> {
            jobService.schedule(j);
            return ok(createFor(j));
        }).orElse(badRequest().body(jobAlreadyExists(job)));
    }

    @GetMapping
    public Collection<Job> listJobs() {

        return jobService.getAllJobs();
    }

    @DeleteMapping("/api/jobs/{job-name}")
    public ResponseEntity<?> deleteJob(@PathVariable("job-name") String jobName) {

        return jobService.getJobByName(jobName).map(j -> {
            jobService.unschedule(j);
            return ok(String.format("Job %s removed", jobName));
        }).orElse(notFound().build());
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class JobCreationResponse {

        private Job job;
        private String errorMessage;

        public Job getJob() {
            return job;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        private JobCreationResponse(Job job) {
            this.job = job;
        }

        private JobCreationResponse(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public static JobCreationResponse createFor(Job job) {
            return new JobCreationResponse(job);
        }

        public static JobCreationResponse jobAlreadyExists(Job job) {
            return new JobCreationResponse(String.format("Job %s already exists", job.getName()));
        }
    }
}
