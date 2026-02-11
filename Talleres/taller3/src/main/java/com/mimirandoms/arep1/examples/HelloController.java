package com.mimirandoms.arep1.examples;

import com.mimirandoms.arep1.GetMapping;
import com.mimirandoms.arep1.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public static String index() {
        return "Greetings from Spring Boot!";
    }
}