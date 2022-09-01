package com.example.restaurant.naver.datalab.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
public class DatalabTotalSearchResponse {
    

    private String startDate;
    private String endDate;
    private String timeUnit;
    private List<Result> results;

    @ToString
    @Data
    public static class Result{
        private String title;
        private List<String> keywords;
        private List<DataObject> data;

        @ToString
        @Data
        public static class DataObject{
            private String period;
            private String ratio; 
        }
    }
}
