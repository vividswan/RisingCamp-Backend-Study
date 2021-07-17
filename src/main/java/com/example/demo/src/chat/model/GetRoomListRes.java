package com.example.demo.src.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetRoomListRes {
    private String profileImageUrl;
    private String nickname;
    private String townAddress;
    private String createdAt;
    private String message;
    private String thumbnail;
    private Long roomNumber;
}
