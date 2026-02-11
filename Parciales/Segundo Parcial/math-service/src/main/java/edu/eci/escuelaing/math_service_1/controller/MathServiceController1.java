package edu.eci.escuelaing.math_service_1.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collatzsequence")
public class MathServiceController1 {
    @GetMapping
    public String collatz(@RequestParam String value) {
        try {
            int intValue = Integer.parseInt(value);
            if (intValue <= 0) {
                return "{\"error\":\"El numero debe ser positivo.\"}";
            }
            String result = calc(intValue, value);
            return "{" +
                    "\"operation\":\"collatzsequence\"," +
                    "\"input\":\"" + value + "\"," +
                    "\"result\":\"" + result + "\"" +
                    "}";
        } catch (NumberFormatException e) {
            return "{\"error\":\"error parseando a entero\"}";
        }
    }

    private String calc(int value, String result) {
        if (value == 1) {
            return result;
        } else if (value % 2 == 0) {
            return calc(value / 2, result + " -> " + value / 2);
        } else {
            return calc(3 * value + 1, result + " -> " + (3 * value + 1));
        }
    }

}
