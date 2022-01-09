package com.sihun.exe1;

import com.sihun.exe1.SprayBottle;
import com.sihun.exe1.SprayHead;
import com.sihun.exe1.sprayHeadSpeed;
import com.sihun.exe1.BottleSize;

public class Waterspray {

    private SprayHead head;
    private SprayBottle body;

    public Waterspray(SprayHead head, SprayBottle body){
        this.head = head;
        this.body = body;
    }

    public Waterspray(sprayHeadSpeed speed, BottleSize size){
        switch(speed){
            case SLOW:
                this.head = new SprayHead(1);
                break;
            case MEDIUM:
                this.head = new SprayHead(10);
                break;
            case Fast:
                this.head = new SprayHead(50);
                break;
        }

        switch(size){
            case BIG:
                body = new SprayBottle(300);
                break;
            case MIDDLE:
                body = new SprayBottle(200);
                break;
            case SMALL:
                body = new SprayBottle(100);
                break;
        }
    }

    public int getRemainingWater() {
        return this.body.getRemainingWater();
    }

    public void setRemainingWater(int remainingWaterInMl){
        this.body.setRemainingWater(remainingWaterInMl);
    }

    public int getOnceAmountspray(){
        return this.head.getOnceAmountspray();
    }

    public int spray(){
        return this.head.sprayFrom(body);
    }

    public int getCapacity(){
        return this.body.getCapacity();
    }

    public void fillUp(){
        this.body.fillUp();
    }

    public void addWater(int amount){
        this.body.addWater(amount);
    }
}

