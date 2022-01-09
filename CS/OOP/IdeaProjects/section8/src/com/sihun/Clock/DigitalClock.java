package com.sihun.Clock;

public class DigitalClock extends Clock {


    public boolean isBeforeMidday(){
        return super.seconds / (DAY_IN_SECONDS / 2) == 0;
    }

    public byte getHours() {
        int hours = this.seconds / 60 / 60;
        if(!super.mode)hours %= 12;
        return hours == 0 ? 12 : (byte)hours;
    }

    private void setTotal(int value){
        value += super.seconds;
        while(value < 0){
            value += DAY_IN_SECONDS;
        }

        super.seconds = value % DAY_IN_SECONDS;
    }

    public void setHours(byte hours) {
        int value = (hours - 1) * 3600;
        setTotal(value);
    }

    public void setMinutes(byte minutes) {
        int value = minutes * 60;
        setTotal(value);
    }

    public void setSeconds(byte seconds) {
        setTotal(seconds);
    }

    public void setTime(byte hours, byte minutes, byte seconds){
        setHours(hours);
        setMinutes(minutes);
        setSeconds(seconds);
    }

    public SevenSegmentDisplay[] getHoursDisplay(){
        return convertToTwoDigitDisplay(getHours());
    }

    public SevenSegmentDisplay[] getMinutesDisplay(){
        return convertToTwoDigitDisplay(getHours());
    }

    public SevenSegmentDisplay[] getSecondsDisplay(){
        return convertToTwoDigitDisplay(getHours());
    }

    private SevenSegmentDisplay[] convertToTwoDigitDisplay(byte number){
        SevenSegmentDisplay[] displays = new SevenSegmentDisplay[2];

        for(int i = 1; i >= 0; i--){
            byte digit = (byte)(number % 10);
            displays[i] = new SevenSegmentDisplay(digit);
            number /= 10;
        }

        return displays;
    }
}
