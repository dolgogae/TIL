package com.sihun;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public void writeByte(String relativePath, byte b){
        Path path = Paths.get(String.valueOf(getClass()), relativePath);

        FileOutputStream out = null;
        try{
            out = new FileOutputStream(new File(path.toString()), true);
        } catch (IOException e){
            e.printStackTrace();
            return;
        } finally {
            if (out != null){
                try{
                    out.close();
                } catch (Exception e){

                }
            }
        }
        // finally로 어떻게든 해제해주면 좋다.
    }

    public static void main(String[] args) {

    }
}

// 1. try 블록의 실행이 중단됨
// 2. catch 블록 중에 발생한 예외를 처리할 수 있는 블록이 있는 지 찾음

// 3.1. 예외를 처리할 수 있는 catch 블록이 없다면 finally블록을 실행 후, 한 단계 높은 try 블록으로 전달
// 3.2 예외를 처리할 수 있는 catch블록이 있다면, 해당 catch 블록안의 코드 실생, finally 블록 실행, try 블록 이후의 코드들이 실행