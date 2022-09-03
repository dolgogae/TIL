package com.example.springcalculator.component;

import org.springframework.stereotype.Component;

@Component
public class Calculator {
    private final  ICalculator iCalculator;

    public Calculator(ICalculator iCalculator){
        this.iCalculator = iCalculator;
    }

    public int sum(int x, int y){
        iCalculator.init();
        return this.iCalculator.sum(x, y);
    }

    public int sub(int x, int y){
        iCalculator.init();
        return this.iCalculator.sub(x, y);
    }
}
