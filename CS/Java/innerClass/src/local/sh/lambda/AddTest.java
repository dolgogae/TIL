package local.sh.lambda;

public class AddTest {
    public static void main(String[] args) {
        Add add1 = (x,y) -> {return x+y;};

        System.out.println(add1.add(2, 6));
    }
}
