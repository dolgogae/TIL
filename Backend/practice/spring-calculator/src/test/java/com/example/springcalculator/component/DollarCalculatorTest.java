package com.example.springcalculator.component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * 필요한 객체를 주입받고 싶을때 @Import를 사용하면 된다.
 */
@SpringBootTest
class DollarCalculatorTest {

    /**
     * spring에서는 Bean으로 관리하기 때문에
     * @Mock이 아닌 @MockBean으로 mocking처리를 한다.
     */
    @MockBean
    private MarketApi marketApi;

    @Autowired
    private Calculator calculator;

    @Test
    public void dollarCalculatorTest(){
//        dollarCalculator.init();
        Mockito.when(marketApi.connect()).thenReturn(3000);

        int sum = calculator.sum(10, 20);
        int sub = calculator.sub(10, 10);

        Assertions.assertEquals(sum, 10*3000 + 20*3000);

        Assertions.assertEquals(sub, 0);
    }


}