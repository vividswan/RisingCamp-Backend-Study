package com.example.demo.src.townlife.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetTownLifeRes {
    private String profileImageUrl;
    private String nickname;
    private String address;
    private Integer certiCount;
    private String updatedAt;
    private String content;
    private Integer sympathyCount;
}
