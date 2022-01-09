package com.sihun.Clock;

public class AnalogClock extends Clock{
//    private byte hours = 12;
//    private byte minutes = 0;
//    private byte seconds = 0;
    // 곂치는 것은 지우는게 좋다.
//    private short hourHandAngle;
//    private short minuteHandAngle;
//    private short secondHandAngle;

    public byte getHours() {
        int hours = this.seconds / 60 / 60;
        if(!super.mode)hours %= 12;
        return hours == 0 ? 12 : (byte)hours;
    }

    public void addSeconds(int seconds){
        int value = seconds + super.seconds;
        while(value < 0){
            value += DAY_IN_SECONDS;
        }

        super.seconds = value % DAY_IN_SECONDS;
    }


    // 쓸데없이 복잡하다.
    // 변경해야하는 상태가 너무 많다.
//    public void addSeconds(short seconds){
//        final int HALF_DAY_IN_SECONDS = 60 * 60 * 12;
//
//        int value = this.seconds + seconds;
//        while(value < 0){
//            value += HALF_DAY_IN_SECONDS;
//        }
//
//        this.seconds = (byte) (value % 60);
//        value /= 60;
//        value += this.minutes;
//        this.minutes = (byte) (value % 60);
//        value /= 60;
//        value += this.hours - 1;
//        this.hours = (byte) (value % 12 + 1);
//    }

//    public void setHours(byte hours) {
//        // jvm까지 가서야 알 수 있다.
//        // 호출하는 함수에게 책임전가? -> 예외를 안던지게끔?
////        if(hours <= 0 || hours > 12){
////            throw new IllegalArgumentException("hours must be [1, 12]");
////        }
//
//        // 1. 입력값이 최솟값, 최댓값을 넘지 못하게 한다.(제한하기)
////        this.hours = (byte) Math.min(Math.max(1, hours), 12); // clamp(hours, min, max)라는 함수를 만들어서 처리 하면 더 좋다.
//        // 2. 최솟값을 넘으면 최댓값, 최솟값을 넘으면 최솟값으로 래핑(wrap)한다.
//        int value = hours - 1;
//
//        while(value < 0){
//            value += 12;
//        }
//        this.hours = (byte) (value % 12 + 1);
//
////        this.hours = hours;
//    }
//
//    public void setMinutes(byte minutes) {
//        int count = 0;
//        while (minutes < 0){
//            minutes += 60;
//            ++count;
//        }
//        count += count / 60;
//
//        if(count != 0)
//            setHours((byte)(this.hours-count));
//
//        this.minutes = (byte) (minutes % 60);
//
////        this.minutes = minutes;
//    }
//
//    public void setSeconds(byte seconds) {
//        int count = 0;
//        while(seconds < 0){
//            seconds += 60;
//            ++count;
//        }
//        count += count / 60;
//
//        if(count != 0)
//            setMinutes((byte)(this.minutes-count));
//
//        this.seconds = (byte) (seconds % 60);
//    }

//    public void setTime(byte hours, byte minutes, byte seconds){
//        setHours(hours);
//        setMinutes(minutes);
//        setSeconds(seconds);
//    }


}
