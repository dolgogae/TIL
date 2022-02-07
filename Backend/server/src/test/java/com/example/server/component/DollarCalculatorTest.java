package com.example.server.component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.ConsoleMockitoLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({MarketApi.class, DollarCalculator.class})
public class DollarCalculatorTest {

    @MockBean
    private MarketApi marketApi;

    @Autowired
    private DollarCalculator dollarCalculator;

    @Test
    public void dollarCalculatorTest(){
        Mockito.when(marketApi.connect()).thenReturn(3000);
        dollarCalculator.init();

        int sum = dollarCalculator.sum(10,10);
        int sub = dollarCalculator.sub(10,10);

        Assertions.assertEquals(60000, sum);
        Assertions.assertEquals(0, sub);
    }

}
