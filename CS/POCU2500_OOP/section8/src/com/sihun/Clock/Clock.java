package com.sihun.Clock;

public class Clock {
    protected static final int DAY_IN_SECONDS = 60 * 60 * 24;
    protected static final int HALF_DAY_IN_SECONDS = 60 * 60 * 12;
    protected int seconds;
    protected boolean mode;

    public byte getHours() {
        int hours = this.seconds / 60 / 60;
        return hours == 0 ? 12 : (byte)hours;
    }

    public byte getMinutes() {
        return (byte) (this.seconds / 60 % 60);
    }

    public byte getSeconds() {
        return (byte) (seconds % 60);
    }


    public void tick(){
        this.seconds = (seconds + 1) % DAY_IN_SECONDS;
    }

    public void mount(){
        //
    }
}
