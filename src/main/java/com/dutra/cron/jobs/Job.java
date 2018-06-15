package com.dutra.cron.jobs;

import com.dutra.cron.jobs.validation.Cron;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;

@JsonSerialize
public class Job implements Runnable {

    @NotNull(message = "Job name cannot be null")
    private String name;
    @NotNull(message = "Job message cannot be null")
    private String message;
    @Cron(message = "A valid format should be like * 0 9 * * *")
    private String cron;

    @JsonCreator
    public Job(String name, @JsonProperty("msg") String message, String cron) {
        this.name = name;
        this.message = message;
        this.cron = cron;
    }

    public String getName() {
        return name;
    }

    public String getCron() {
        return cron;
    }

    @Override
    public void run() {
        System.out.println(message);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(message)
                .toString();
    }
}
