package com.example.restaurant.wishlist.entity;

import com.example.restaurant.db.MemoryDbEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WishListEntity extends MemoryDbEntity {
    private String title;
    private String category;
    private String address;
    private String roadAddress;
    private String homePageLink;
    private String ImageLink;
    private boolean isVisit;
    private int visitCount;
    private LocalDateTime lastVisitDate;
}
