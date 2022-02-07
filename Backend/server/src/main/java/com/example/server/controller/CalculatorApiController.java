package com.example.server.controller;

import com.example.server.component.Calculator;
import com.example.server.component.ICalculator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CalculatorApiController {

    private Calculator calculator;

    @GetMapping("/sum")
    public int sum(@RequestParam int x, @RequestParam int y){
        return calculator.sum(x,y);
    }

    @GetMapping("/sub")
    public int sub(@RequestParam int x, @RequestParam int y){
        return calculator.sub(x,y);
    }
}
