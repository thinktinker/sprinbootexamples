package com.martin.gettingstarted.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/")
    public String index(){
        return "Greeting from Spring Boot!";
    }

    @GetMapping("/greetings")
    public String greetings(){
        return "Greetings from Martin";
    }

}
