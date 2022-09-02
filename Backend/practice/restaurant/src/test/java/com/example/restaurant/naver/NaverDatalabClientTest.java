package com.example.restaurant.naver;

import com.example.restaurant.naver.datalab.NaverDatalabClient;
import com.example.restaurant.naver.datalab.dto.DatalabTotalSearchRequest;
import com.example.restaurant.naver.datalab.dto.DatalabTotalSearchRequestBuilder;
import com.example.restaurant.naver.datalab.dto.DatalabTotalSearchResponse;
import com.example.restaurant.naver.datalab.dto.KeywordGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NaverDatalabClientTest {
    
    @Autowired
    NaverDatalabClient naverDatalabClient;

    @Test
    void searchTest() throws JsonProcessingException{
        DatalabTotalSearchRequest request = new DatalabTotalSearchRequestBuilder()
            .setStartDate("2017-01-01")
            .setEndDate("2017-04-30")
            .setTimeUnit("month")
            .addKeywordGroups(new KeywordGroup().setGroupName("한글").addKeywords("한글").addKeywords("korean"))
            .addKeywordGroups(new KeywordGroup().setGroupName("영어").addKeywords("영어").addKeywords("english"))
            .setDevice("pc")
            .addAge("1").addAge("2")
            .setGender("f")
            .build();

        String params = new ObjectMapper().writeValueAsString(request);

        System.out.println("request:" + params);

        DatalabTotalSearchResponse resposne = naverDatalabClient.searchDatalab(request);

        String res = new ObjectMapper().writeValueAsString(resposne);

        System.out.println("response: " + res);
    }
}
