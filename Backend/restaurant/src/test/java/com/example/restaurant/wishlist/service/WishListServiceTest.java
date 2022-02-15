package com.example.restaurant.wishlist.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WishListServiceTest {

    @Autowired
    private WishListService wishListService;

    @Test
    public void searchTest(){
        var result = wishListService.search("갈비집");

        System.out.println(result);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getAddress(), "부산광역시 해운대구 중동  1225-1");
    }
}
