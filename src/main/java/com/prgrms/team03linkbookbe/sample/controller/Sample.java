package com.prgrms.team03linkbookbe.sample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class Sample {

    @GetMapping("/sample")
    String greeting(){
        return "Sample!";
    }

}
