package com.example.restaurant.naver.datalab;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import com.example.restaurant.naver.datalab.dto.DatalabTotalSearchRequest;
import com.example.restaurant.naver.datalab.dto.DatalabTotalSearchResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Component
public class NaverDatalabClient {
    
    @Value("${naver.client.datalab.id}")
    private String id;

    @Value("${naver.client.datalab.secret}")
    private String passwd;

    @Value("${naver.url.datalab.total}")
    private String url;

    public static<T> ResponseEntity<T> search(
        Object request,
        ParameterizedTypeReference<T> responseType,
        String url, String id, String passwd) throws JsonProcessingException{

        URI uri = UriComponentsBuilder.fromUriString(url)
            .build().encode().toUri();
        
        HttpHeaders header = getHttpHeaders(id, passwd, MediaType.APPLICATION_JSON);

        String params = new ObjectMapper().writeValueAsString(request);

        HttpEntity<String> httpEntity = new HttpEntity<>(params, header);

        var responseEntity = new RestTemplate().exchange(
                uri,
                HttpMethod.POST,
                httpEntity,
                responseType
        );

        return responseEntity;
    }

    public static HttpHeaders getHttpHeaders(String id, String passwd, MediaType contentType){
        HttpHeaders header = new HttpHeaders();
        header.set("X-Naver-Client-Id", id);
        header.set("X-Naver-Client-Secret", passwd);
        header.setContentType(contentType);
        return header;
    }

    public DatalabTotalSearchResponse searchDatalab(DatalabTotalSearchRequest request) throws JsonProcessingException{
        var responseType =  new ParameterizedTypeReference<DatalabTotalSearchResponse>() {};
        var responseEntity = search(request, responseType, url, id, passwd);

        return responseEntity.getBody();
    }
}
