package com.example.restaurant.naver.datalab.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatalabTotalSearchRequest {
    
    private String startDate;
    private String endDate;
    private String timeUnit;
    private List<KeywordGroup> keywordGroups;
    private String device;
    private String gender;
    private List<String> ages;

}
