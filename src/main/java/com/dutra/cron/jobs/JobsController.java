package com.dutra.cron.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
public class JobsController {

    @Autowired
    private JobService jobService;

    @GetMapping("/")
    public String index(Model model) {
        Collection<Job> allJobs = jobService.getAllJobs();
        model.addAttribute("jobs", allJobs);

        return "index";
    }
}
