package com.sihun.exe1;

public class SprayBottle {

    private int remainingWaterInMl = 0;
    private int capacity;

    public SprayBottle(int capacity){
        this.capacity = capacity;
    }

    public int getRemainingWater() {
        return this.remainingWaterInMl;
    }

    public void setRemainingWater(int remainingWaterInMl){
        this.remainingWaterInMl = remainingWaterInMl;
    }

    public int getCapacity(){
        return this.capacity;
    }

    public void fillUp(){
        this.remainingWaterInMl = this.capacity;
    }

    public void addWater(int amount){
        remainingWaterInMl += amount;
        if(remainingWaterInMl > capacity)
            remainingWaterInMl = capacity;
    }

    public void reduceWater(int amount){
        remainingWaterInMl -= amount;
        if(remainingWaterInMl < 0)
            remainingWaterInMl = 0;
    }
}
