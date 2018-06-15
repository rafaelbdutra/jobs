package com.dutra.cron.jobs.validation;

import com.dutra.cron.jobs.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

import static java.util.stream.Collectors.toList;

@Component
public class JobService {

    @Autowired
    private TaskScheduler taskScheduler;

    private Map<String, JobTuple> jobs = new HashMap();

    public Optional<Job> accept(Job job) {
        if (jobs.containsKey(job.getName())) {
            return Optional.empty();
        }
        return Optional.of(job);
    }

    public void schedule(Job job) {
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(job, new CronTrigger(job.getCron()));
        jobs.put(job.getName(), new JobTuple(job, scheduledFuture));
    }

    public void unschedule(Job job) {
        jobs.get(job.getName()).getScheduledFuture().cancel(false);
        jobs.remove(job.getName());
    }

    public Collection<Job> getAllJobs() {
        return Collections.unmodifiableCollection(jobs.values().stream().map(JobTuple::getJob).collect(toList()));
    }

    public Optional<Job> getJobByName(String jobName) {
        if (jobs.containsKey(jobName)) {
            return Optional.of(jobs.get(jobName).getJob());
        }
        return Optional.empty();
    }

    static class JobTuple {

        private Job job;
        private ScheduledFuture scheduledFuture;

        public JobTuple(Job job, ScheduledFuture scheduledFuture) {
            this.job = job;
            this.scheduledFuture = scheduledFuture;
        }

        public Job getJob() {
            return job;
        }

        public ScheduledFuture getScheduledFuture() {
            return scheduledFuture;
        }
    }

}
