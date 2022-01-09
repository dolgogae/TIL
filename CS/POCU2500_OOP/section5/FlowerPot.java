package com.sihun.exe1;

import com.sihun.exe1.Waterspray;

public class FlowerPot {
    private boolean alive = true;
    private int minDailyWaterInMl;
    private int dailyWaterReceived;

    public FlowerPot(int minDailyWaterInMl){
        this.minDailyWaterInMl = minDailyWaterInMl;
    }

    public boolean isAlive(){
        return this.alive;
    }

    public int getMinDailyWaterInMl(){
        return this.minDailyWaterInMl;
    }

    public void addWater(Waterspray ws){
        dailyWaterReceived += ws.spray();
    }

    public void liveAnotherDay(){
        if(dailyWaterReceived < minDailyWaterInMl)
            alive = false;
        dailyWaterReceived = 0;
    }
}
