package com.example.server.controller;

import com.example.server.component.Calculator;
import com.example.server.component.DollarCalculator;
import com.example.server.component.ICalculator;
import com.example.server.component.MarketApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(CalculatorApiController.class)
@AutoConfigureWebMvc
@Import({Calculator.class, DollarCalculator.class})
public class CalculatorApiControllerTest {
    @MockBean
    private MarketApi marketApi;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        Mockito.when(marketApi.connect()).thenReturn(3000);
    }

    @Test
    public void sumTest(){
        // http://localhost:8080/api/sum

        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.get("http://localhost:8080/api/sum")
                            .queryParam("x", "10")
                            .queryParam("y", "10")
            ).andExpect(
                    MockMvcResultMatchers.status().isOk()
            ).andExpect(
                    MockMvcResultMatchers.content().string("60000")
            ).andDo(
                    MockMvcResultHandlers.print()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}