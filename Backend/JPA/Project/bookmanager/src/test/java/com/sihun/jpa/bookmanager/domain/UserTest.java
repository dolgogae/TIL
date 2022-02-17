package com.sihun.jpa.bookmanager.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


class UserTest {
    @Test
    void test(){
        User user = new User();
        user.setEmail("sihun@sihun.com");
        user.setName("sihun");

        User user1 = new User(null, "sihun", "sihun@sihun.com", LocalDateTime.now(), LocalDateTime.now());
        User user2 = new User("sihun", "sihun@sihun.com");

        User user3 = User.builder()
                .name("sihun")
                .email("sihun@sihun.com")
                .build();

        System.out.println(">>> " + user.toString());
    }
}