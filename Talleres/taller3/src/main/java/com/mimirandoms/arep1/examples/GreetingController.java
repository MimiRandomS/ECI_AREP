package com.mimirandoms.arep1.examples;

import com.mimirandoms.arep1.GetMapping;
import com.mimirandoms.arep1.RequestParam;
import com.mimirandoms.arep1.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hola " + name;
    }
}