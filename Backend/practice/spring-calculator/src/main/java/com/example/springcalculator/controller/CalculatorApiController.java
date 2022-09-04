package com.example.springcalculator.controller;

import com.example.springcalculator.component.Calculator;
import com.example.springcalculator.dto.RequestDto;
import com.example.springcalculator.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CalculatorApiController {

    private final Calculator calculator;

    @GetMapping("/sum")
    public int sum(@RequestParam int x, @RequestParam int y){
        return this.calculator.sum(x, y);
    }

    @GetMapping("/sub")
    public int sub(@RequestParam int x, @RequestParam int y){
        return this.calculator.sub(x, y);
    }

    @PostMapping("/sub")
    public ResponseDto sub2(@RequestBody RequestDto request){

        int x = request.getX();
        int y = request.getY();

        ResponseDto response = new ResponseDto();
        response.setResult(this.calculator.sub(x, y));

        return response;
    }

}
