package com.dutra.cron.jobs.validation;

import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class CronValidator implements ConstraintValidator<Cron, String> {

    @Override
    public boolean isValid(String cronExpression, ConstraintValidatorContext constraintValidatorContext) {
        return CronSequenceGenerator.isValidExpression(cronExpression);
    }
}
