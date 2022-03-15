public class Main {
    public static void main(String[] args) {
        System.out.println("hello Junit");

        ICalculator iCalculator = new KrwCalulator();
        Calculator calculator = new Calculator(iCalculator);

    }
}
