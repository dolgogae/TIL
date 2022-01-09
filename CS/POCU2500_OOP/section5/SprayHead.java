package com.sihun.exe1;

import com.sihun.exe1.SprayBottle;

public class SprayHead {

    private int onceAmountspray;

    public SprayHead(int onceAmountspray){
        this.onceAmountspray = onceAmountspray;
    }

    public int getOnceAmountspray(){
        return this.onceAmountspray;
    }

    public int sprayFrom(SprayBottle body){
        body.reduceWater(onceAmountspray);
        return onceAmountspray;
    }
}
