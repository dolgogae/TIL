import component.Calculator;
import component.DollarCalculator;
import component.MarketApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * 내가 호출한 method가 원하는 결과값을 호출하는지 확인할 때 mocking 처리를 한다고 한다.
 *
 *
 */
@ExtendWith(MockitoExtension.class)
public class DollarCalculatorTest {

    /**
     * market api는 상황에 따라서 값이 달라질 수 있다.(달러의 시세라고 가정하면
     * 따라서 mocking 처리를 하게 되면 호출을 막고 일정한 값이 나오도록 설정할 수 있다.
     */
    @Mock
    public MarketApi marketApi;

    /**
     * mocking처리한 변수를 다음처럼 3000으로 고정값 설정을 해준다.
     */
    @BeforeEach
    public void init(){
        Mockito.lenient().when(marketApi.connect()).thenReturn(3000);
    }

    @Test
    public void testHello(){
        System.out.println("hello");
    }

    @Test
    public void dollarTest(){
        MarketApi marketApi = new MarketApi();
        DollarCalculator dollarCalculator = new DollarCalculator(marketApi);
        dollarCalculator.init();

        Calculator calculator = new Calculator(dollarCalculator);

        System.out.println(calculator.sum(10,10));

        Assertions.assertEquals(22000, calculator.sum(10,10));
        Assertions.assertEquals(0, calculator.sub(10,10));
    }


    @Test
    public void mockTest(){
        DollarCalculator dollarCalculator = new DollarCalculator(marketApi);
        dollarCalculator.init();

        Calculator calculator = new Calculator(dollarCalculator);

        System.out.println(calculator.sum(10,10));

        Assertions.assertEquals(60000, calculator.sum(10,10));
        Assertions.assertEquals(0, calculator.sub(10,10));
    }
}
