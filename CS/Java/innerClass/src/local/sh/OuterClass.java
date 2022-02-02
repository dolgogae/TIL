package local.sh;

public class OuterClass {
    private int sNum = 10;
    InClass inClass;

    public OuterClass(){
        inClass = new InClass();
    }

    public class InClass{
        int iNum = 100;
//        static int sInNum = 500;

        void inTest() {
            // 자동으로 생성되기 때문에 호출 가능
            System.out.println("hello" + sNum);
        }
    }

    public static class InStaticClass{
        int iNum = 100;
        static int sInNum = 200;

        void inTest() {
            // 외부클래스의 인스턴스 변수를 못쓴다. 생성을 못했을 수도 있기 때문
//            System.out.println("Hello" + sNum);
        }

        static void sTest(){

        }
    }

    public void usingClass(){
        inClass.inTest();
    }
}
