package com.sihun.exe1;

import com.sihun.exe1.Waterspray;
import com.sihun.exe1.FlowerPot;

public class Main {
    public static void main(String[] args) {
        Waterspray ws = new Waterspray(sprayHeadSpeed.Fast, BottleSize.BIG);
        FlowerPot fp = new FlowerPot(100);

        ws.fillUp();

        while(fp.isAlive()){
            fp.addWater(ws);
        }
    }
}
