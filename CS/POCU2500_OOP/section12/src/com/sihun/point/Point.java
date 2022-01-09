package com.sihun.point;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    // 복사 생성자
    public Point(final Point other){
        this(other.x, other.y);
    }
}
