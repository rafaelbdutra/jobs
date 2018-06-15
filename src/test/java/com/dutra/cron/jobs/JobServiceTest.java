package com.dutra.cron.jobs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JobServiceTest {

    private static final String JOB_NAME = "my-job";
    private static final String JOB_MESSAGE = "some message";
    private static final String JOB_CRON = "* * * * * *";

    @Mock
    private TaskScheduler taskScheduler;
    @Mock
    private ScheduledFuture scheduledFuture;

    private Job validJob;

    private JobService jobServiceTest;

    @Before
    public void setUp() {
        Mockito.when(taskScheduler.schedule(any(Runnable.class), any(CronTrigger.class)))
                .thenReturn(scheduledFuture);

        jobServiceTest = new JobService(taskScheduler);
        validJob = new Job(JOB_NAME, JOB_MESSAGE, JOB_CRON);
    }

    @Test
    public void shouldAcceptJob() {
        Optional<Job> optionalJob = jobServiceTest.accept(validJob);

        assertTrue(optionalJob.isPresent());
    }

    @Test
    public void shouldNotAcceptIfJobAlreadyExists() {
        jobServiceTest.schedule(validJob);
        Optional<Job> optionalJob = jobServiceTest.accept(validJob);

        assertFalse(optionalJob.isPresent());
    }

    @Test
    public void shouldScheduleJob() {
        jobServiceTest.schedule(validJob);

        verify(taskScheduler).schedule(any(Runnable.class), any(CronTrigger.class));

        Optional<Job> scheduledJob = jobServiceTest.getJobByName(JOB_NAME);
        assertTrue(scheduledJob.isPresent());
    }

    @Test
    public void shouldUnscheduleJob() {
        jobServiceTest.schedule(validJob);
        jobServiceTest.unschedule(validJob);

        verify(scheduledFuture).cancel(false);

        Optional<Job> scheduledJob = jobServiceTest.getJobByName(JOB_NAME);
        assertFalse(scheduledJob.isPresent());
    }

    @Test
    public void shouldNotExistJob() {
        Optional<Job> scheduledJob = jobServiceTest.getJobByName(JOB_NAME);
        assertFalse(scheduledJob.isPresent());
    }

    @Test
    public void shouldGetJobByName() {
        jobServiceTest.schedule(validJob);

        Optional<Job> scheduledJob = jobServiceTest.getJobByName(JOB_NAME);
        assertTrue(scheduledJob.isPresent());
    }

}