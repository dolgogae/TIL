package com.sihun.Clock;

public class SevenSegmentDisplay {
    private byte segmentDigit;

    public SevenSegmentDisplay(byte digit){

        switch(digit){
            case 1: segmentDigit += DisplayDigit.getB() + DisplayDigit.getC();
                break;
                // // digit에 따라 7개의 선분을 알맞게 on/off
        }

    }
}
