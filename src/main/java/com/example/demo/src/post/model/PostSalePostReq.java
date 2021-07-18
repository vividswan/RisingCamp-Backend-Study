package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostSalePostReq {
    private String status;
    private String title;
    private String content;
    private String category;
    private Integer salePrice;
    private Boolean priceSuggestion;
}
