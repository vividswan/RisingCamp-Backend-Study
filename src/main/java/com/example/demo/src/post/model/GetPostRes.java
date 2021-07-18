package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetPostRes {
    private String nickname;
    private String townAddress;
    private Float mannerTemperature;
    private String saleStatus;
    private String title;
    private String category;
    private String isRaised;
    private String updatedAt;
    private String content;
    private Integer likeCount;
    private Integer viewCount;
    private Boolean likeCheck;
    private Integer salePrice;
    private Boolean priceSuggestion;
}
