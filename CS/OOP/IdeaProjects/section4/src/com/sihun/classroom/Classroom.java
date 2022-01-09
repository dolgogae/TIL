package com.sihun.classroom;

public class Classroom {
    private int[] scores;
    private float mean;

    public boolean setScore(int index, int score){
        // index 범위 체크 코드 생략

        scores[index] = score;
        updateMean(); // mean은 setScore를 한 행위로 바뀜 -> 이상적인 setter
        
        return true;
    }

    private void updateMean(){
        this.mean = /*계산결과*/ 10;
    }
}
