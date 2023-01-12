package local.sh;

public class Main {
    public static void main(String[] args) {
        OuterClass outerClass = new OuterClass();
        outerClass.usingClass();

        // 정적 클래스는 바로 생성 가능
        OuterClass.InStaticClass sInClass = new OuterClass.InStaticClass();
        sInClass.inTest();

    }
}
