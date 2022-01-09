package com.sihun.imageLoad;

import java.awt.*;

public class Image {
    //========================
    // eager Loading
    //========================
//    private  ImageData image;
//
//    // 생성자에서 무조건 이미지를 읽어옴
//    // 메모리를 많이 사용, 이미지를 읽어오는데 시간도 걸림
//    public Image(String filePath){
//        this.image = ImageLoader.getInstance().load(filePath);
//    }
//
//    public void draw(Canvas canvas, int x, int y) {
//
//    }


    //============================
    // proxy pattern(lazy loading)
    //============================
    private String filePath;
    private ImageData image;

    public Image(String filePath){
        this.filePath = filePath;
    }

    public boolean isLoaded(){return this.image != null;}

    ///////////////////////////////////////////////////////////////
    //                  클라이언트가 직접 할수 있게                    //
    ///////////////////////////////////////////////////////////////

    public void load(){
        if(this.image == null){
            this.image = ImageLoader.getInstance().load(this.filePath);
        }
    }

    public void unload(){this.image=null;}
    ///////////////////////////////////////////////////////////////

    public void draw(Canvas canvas, int x, int y){
//        if(this.image == null){
//            this.image = ImageLoader.getInstance().load(this.filePath);
//        }

        canvas.draw(this.image, x, y);
    }

    // 요즘 세상의 프록시 패턴
    // 요즘 컴퓨터에는 메모리를 많이 장착
    // 한번에 그리는 이미지 수가 많지 않다면?
    // 인터넷에서 그 이미지들을 로딩하면?

    // 클라이언트가 내부 동작방법을 분명히 알고 그에 적합한 UI를 보여주는 방법이 더 사랑받는 중
    // 클래스가 남몰래 프록시 패턴을 사용하는 것보다, 클라이언트에게 조작 권한을 주는게 좋음
}
