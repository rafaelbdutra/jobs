# language: en
Feature: Scheduled jobs
    Creating, Listing and deleting scheduled jobs

    Scenario: Create and List scheduled job
        Given a previous created scheduled job
        When calling list endpoint
        Then scheduled job should be returned
        And http status should be OK

    Scenario: Delete scheduled job
        Given a previous created scheduled job
        When calling delete endpoint
        And calling list endpoint again
        Then scheduled job should not be returned

    Scenario: Could not create job with invalid cron
        Given a job with invalid cron
        When calling create job
        Then http status should be bad request

    Scenario: Could not create job with existing name
        Given a previous created scheduled job
        When trying to create same job
        Then http status should be bad request

#    Scenario: Creating a scheduled job
#        Given a valid job
#        When accept this job
#        Then the job should be registered