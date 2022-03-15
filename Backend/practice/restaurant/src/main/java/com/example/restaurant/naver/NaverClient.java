package com.example.restaurant.naver;

import com.example.restaurant.naver.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class NaverClient {

    @Value("${naver.client.id}")
    private String naverClientId;

    @Value("${naver.client.secret}")
    private String naverClientSecret;

    @Value("${naver.url.search.local}")
    private String naverLocalSearchUrl;

    @Value("${naver.url.search.image}")
    private String naverImageSearchUrl;

    public static<T> ResponseEntity<T> search(MultiValueMap<String, String> mv,
                                         ParameterizedTypeReference<T> responseType,
                                              String url, String id, String passwd){
        URI uri = UriComponentsBuilder.fromUriString(url)
                .queryParams(mv)
                .build()
                .encode()
                .toUri();

        var header = new HttpHeaders();
        header.set("X-Naver-Client-Id", id);
        header.set("X-Naver-Client-Secret", passwd);
        header.setContentType(MediaType.APPLICATION_JSON);

        var httpEntity = new HttpEntity<>(header);
//        var responseType = new ParameterizedTypeReference<SearchRes>(){};

        var responseEntity = new RestTemplate().exchange(
                uri,
                HttpMethod.GET,
                httpEntity,
                responseType
        );
        return responseEntity;
    }

    public SearchLocalRes searchLocal(SearchLocalReq searchLocalReq){
        var mv = searchLocalReq.toMultiValueMap();
        var responseType = new ParameterizedTypeReference<SearchLocalRes>(){};
        var responseEntity = search(mv, responseType, naverLocalSearchUrl, naverClientId, naverClientSecret);

        return responseEntity.getBody();
    }

    public SearchImageRes searchImage(SearchImageReq searchImageReq){
        var mv = searchImageReq.toMultiValueMap();
        var responseType = new ParameterizedTypeReference<SearchImageRes>(){};
        var responseEntity = search(mv, responseType, naverImageSearchUrl, naverClientId, naverClientSecret);

        return (SearchImageRes) responseEntity.getBody();
    }


}
