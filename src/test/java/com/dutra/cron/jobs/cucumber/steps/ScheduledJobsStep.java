package com.dutra.cron.jobs.cucumber.steps;

import com.dutra.cron.jobs.Job;
import com.dutra.cron.jobs.SpringIntegrationTest;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ScheduledJobsStep extends SpringIntegrationTest {

    private static final String JOB_NAME = "my-job";
    private static final String JOB_MESSAGE = "some message";
    private static final String JOB_CRON = "* * * * * *";
    private static final String INVALID_JOB_CRON = "* * * *";
    
    private Job invalidJob;

    private ResponseEntity<Collection> jobs;
    private ResponseEntity<String> errorResponse;

    @Given("^a previous created scheduled job$")
    public void a_previous_created_scheduled_job() {
        Job job = new Job(JOB_NAME, JOB_MESSAGE, JOB_CRON);

        template.postForEntity("/api/jobs", job, Job.class);
    }

    @When("^calling list endpoint$")
    public void calling_list_endpoint() throws Throwable {
        callListEndpoint();
    }

    private void callListEndpoint() {
        jobs = template.getForEntity("/api/jobs", Collection.class);
    }

    @Then("^scheduled job should be returned$")
    public void scheduled_job_should_be_returned() throws Throwable {
        assertFalse(jobs.getBody().isEmpty());

        Map job = (Map) jobs.getBody().iterator().next();

        assertEquals(JOB_NAME, job.get("name"));
        assertEquals(JOB_MESSAGE, job.get("msg"));
        assertEquals(JOB_CRON, job.get("cron"));
    }

    @And("^http status should be OK$")
    public void http_status_should_be_OK() throws Throwable {
        assertEquals(HttpStatus.OK, jobs.getStatusCode());
    }

    @When("^calling delete endpoint$")
    public void calling_delete_endpoint() {
        template.delete("/api/jobs/" + JOB_NAME);
    }

    @And("^calling list endpoint again$")
    public void calling_list_endpoint_again() throws Throwable {
        callListEndpoint();
    }

    @Then("^scheduled job should not be returned$")
    public void scheduled_job_should_not_be_returned() {
        assertTrue(jobs.getBody().isEmpty());
    }
    
    @Given("^a job with invalid cron$")
    public void a_job_whit_invalid_cron() {
        invalidJob = new Job(JOB_NAME, JOB_MESSAGE, INVALID_JOB_CRON);
    }

    @When("^calling create job")
    public void calling_create_job() {
        errorResponse = template.postForEntity("/api/job", invalidJob, String.class);
    }

    @Then("^http status should be bad request$")
    public void http_status_should_be_bad_request() {
        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
    }

    @When("^trying to create same job$")
    public void trying_to_create_same_job() {
        Job job = new Job(JOB_NAME, JOB_MESSAGE, JOB_CRON);

        errorResponse = template.postForEntity("/api/jobs", job, String.class);
    }

}
