package com.example.demo.src.post.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class GetSalePostRes {
    String photoImageUrl;
    String title;
    String address;
    String isRaised;
    String updatedAt;
    Integer raisedCount;;
    String saleStatus;
    Integer salePrice;
    Integer chatCount;
    Integer likeCount;
    String townAddress;
}
