package com.example.restaurant.naver.datalab.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatalabTotalSearchRequestBuilder {
    
    private String startDate;
    private String endDate;
    private String timeUnit;
    private List<KeywordGroup> keywordGroups = new ArrayList<>();
    private String device;
    private String gender;
    private List<String> ages = new ArrayList<>();


    public DatalabTotalSearchRequestBuilder setStartDate(String startDate){
        this.startDate = startDate;
        return this;
    }

    public DatalabTotalSearchRequestBuilder setEndDate(String endDate){
        this.endDate = endDate;
        return this;
    }

    public DatalabTotalSearchRequestBuilder setTimeUnit(String timeUnit){
        this.timeUnit = timeUnit;
        return this;
    }

    public DatalabTotalSearchRequestBuilder setDevice(String device){
        this.device = device;
        return this;
    }

    public DatalabTotalSearchRequestBuilder setGender(String gender){
        this.gender = gender;
        return this;
    }

    public DatalabTotalSearchRequestBuilder addAge(String age){
        if (this.ages.size() > 2){
            log.info("Already full for arguments");
            return this;
        }
        this.ages.add(age);
        return this;
    }

    public DatalabTotalSearchRequestBuilder addKeywordGroups(KeywordGroup keywordGroup){
        this.keywordGroups.add(keywordGroup);
        return this;
    }

    public DatalabTotalSearchRequest build(){
        DatalabTotalSearchRequest request = new DatalabTotalSearchRequest();
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setTimeUnit(timeUnit);
        request.setKeywordGroups(keywordGroups);
        request.setDevice(device);
        request.setAges(ages);
        request.setGender(gender);

        return request;
    }
}
