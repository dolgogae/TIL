package com.sihun.point;

public final class Line {
    private final Point p1;
    private final Point p2;

    public Line(final Point p1, final Point p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    // 깊은 복사
    public Line(final Line other){
        this(new Point(other.p1), new Point(other.p2));
    }
}
