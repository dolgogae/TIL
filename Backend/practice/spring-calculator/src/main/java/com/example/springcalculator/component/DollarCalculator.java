package com.example.springcalculator.component;

import org.springframework.stereotype.Component;

@Component
public class DollarCalculator implements ICalculator {

    private int price = 1;
    private final MarketApi marketApi;

    public DollarCalculator(MarketApi marketApi){
        this.marketApi = marketApi;
    }

    @Override
    public void init(){
        this.price = marketApi.connect();
    }

    @Override
    public int sum(int x, int y) {
        x *= price;
        y *= price;
        return x+y;
    }

    @Override
    public int sub(int x, int y) {
        x *= price;
        y *= price;
        return x-y;
    }
}
