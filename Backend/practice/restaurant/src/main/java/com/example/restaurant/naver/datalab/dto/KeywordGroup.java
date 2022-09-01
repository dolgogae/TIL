package com.example.restaurant.naver.datalab.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class KeywordGroup{
    private String groupName;
    private List<String> keywords = new ArrayList<>();

    public KeywordGroup setGroupName(String groupName){
        this.groupName = groupName;
        return this;
    }

    public KeywordGroup addKeywords(String keyword){
        keywords.add(keyword);
        return this;
    }
}
