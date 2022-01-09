package com.company;

/*
public class Math {
    private Math(){}

    public static int abs(int n){
        return n > 0 ? n : -n;
    }
    public static int min(int a, int b){
        return a > b ? b : a;
    }

    public static int max(int a, int b){
        return a > b ? a : b;
    }
}
 */


// 이 클래스는 매서드만 있어서 싱글턴의 좋은 예는 아니다.
// static이 더 좋은 예시
public class Math {
    private static Math instance;

    private Math(){}

    public static Math getInstance(){
        if(instance == null)
            instance = new Math();
        return instance;
    }

    public static int abs(int n){
        return n > 0 ? n : -n;
    }
    public static int min(int a, int b){
        return a > b ? b : a;
    }

    public static int max(int a, int b){
        return a > b ? a : b;
    }
}