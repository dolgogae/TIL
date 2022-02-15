package com.example.restaurant.wishlist.dto;

import com.example.restaurant.db.MemoryDbEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishListDto {
    private int index;
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

// 데이터 베이스에 변수가 변경되면 프론트 엔드에 영향을 미치기 때문에 분리해준다.
