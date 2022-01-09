package com.company;

// 싱글턴 생성시 인자가 필요한 경우
// 다른 클래스에서 getInstance를 호출시 반복적으로 인자를 넣게 된다.

// createInstance()가 먼저 호출됐다는 가정하에 getInstance()를 호출해야한다.

// 안티패턴
// 매직 넘버, 매직 스트링 등
// 실질적으로 static과 똑같다는 이유로 욕하는 사람들이 있다.
// 무의미한 주장

import java.awt.*;

public class GraphicsResourceManager {
    private static GraphicsResourceManager instance;

    private GraphicsResourceManager(FileLoader loader, GraphicsDevice gfxDevice){
        // ...
    }

    public static void createInstance(FileLoader loader, GraphicsDevice gfxDevice){
        assert (instance == null): "do not create instace twice";

        instance = new GraphicsResourceManager(loader, gfxDevice);
    }

    public static void deleteInstance(){
        assert (instance != null) : "no instance to delete";

        instance = null;
    }

    public static GraphicsResourceManager getInstance(){
        assert (instance != null) : "no instace was created before get()";

        return instance;
    }
}
