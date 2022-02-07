package com.example.client.service;

import java.net.URI;

import com.example.client.domain.Req;
import com.example.client.domain.UserRequest;
import com.example.client.domain.UserResponse;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class RestTemplateService {

    // http://localhost/api/server/hello
    // respones

    public UserResponse hello(){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost")
                .path("/api/server/hello")
                .queryParam("name", "steve")
                .queryParam("age", 10)
                .encode()
                .build()
                .toUri();
        
        System.out.println(uri.toString());

        RestTemplate restTemplate = new RestTemplate();
        // String result = restTemplate.getForObject(uri, String.class);
        ResponseEntity<UserResponse> result = restTemplate.getForEntity(uri, UserResponse.class);

        System.out.println(result.getStatusCode());
        System.out.println(result.getBody());

        return result.getBody();
    }

    public void post(){
        // http://localhost:9090/api/server/user/{userId}/name/{userName}

        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/user/{userId}/name/{userName}")
                .encode()
                .build()
                .expand(100, "steve")        // 순서대로 매칭된다.
                .toUri();
        System.out.println(uri);

        // http body -> object -> object mappper -> json -> rest template -> http body json
        UserRequest req = new UserRequest();
        req.setName("steve");
        req.setAge(10);

        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<UserResponse> response = restTemplate.postForEntity(uri, req, UserResponse.class);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, req, String.class);

        System.out.println(response.getStatusCode());
        System.out.println(response.getHeaders());
        System.out.println(response.getBody());

//        return response.getBody();
    }

    public UserResponse exchange(){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/user/{userId}/name/{userName}")
                .encode()
                .build()
                .expand(100, "steve")        // 순서대로 매칭된다.
                .toUri();
        System.out.println(uri);

        // http body -> object -> object mappper -> json -> rest template -> http body json
        UserRequest req = new UserRequest();
        req.setName("steve");
        req.setAge(10);

        RequestEntity<UserRequest> requestEntity = RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-authorization", "abcd")
                .header("custom-header", "ffff")
                .body(req);


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserResponse> response = restTemplate.exchange(requestEntity, UserResponse.class);
        return response.getBody();
    }

    public Req<UserResponse> genericExchange(){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/user/{userId}/name/{userName}")
                .encode()
                .build()
                .expand(100, "steve")        // 순서대로 매칭된다.
                .toUri();
        System.out.println(uri);

        // http body -> object -> object mappper -> json -> rest template -> http body json

        UserRequest userRequest = new UserRequest();
        userRequest.setName("steve");
        userRequest.setAge(10);

        Req req = new Req<UserRequest>();
        req.setHeader(
            new Req.Header()
        );
        req.setResBody(
            userRequest
        );

        RequestEntity requestEntity = RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-authorization", "abcd")
                .header("custom-header", "ffff")
                .body(req);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Req<UserResponse>> response
                = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<Req<UserResponse>>(){});

        return response.getBody();
    }
}
