package local.sh;

class Outer2{
    int outNum = 100;
    static int sNum = 200;

    public Runnable getRunnable(int i){
        int num = 10;

        return new Runnable(){
            int localNum = 1000;

            @Override
            public void run() {
//                i = 10;
//                localNum = 20;
                // 메서드는 호출되고 끝나면 스택 메모리는 없어진다.
                // run 이라는 메서드는 재호출 될 수 있는 여지가 있다.
                // 그때, i, localNum를 처리할 수 있어야하므로 메모리에 있어야한다.
                // 따라서, 컴파일러에서 자동으로 final처리를 한다.
            }
        };
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            System.out.println("Runnable Class");
        }
    };
}

public class AnonymousInnerClass {
    public static void main(String[] args) {
        Outer2 out = new Outer2();
        Runnable runner = out.getRunnable(100);

        runner.run();

        out.runnable.run();
    }
}
