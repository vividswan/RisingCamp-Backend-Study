package com.example.demo.src.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostChatReq {
    private String message;
    private String imageUrl;
    private String emoticon;
    private Long postId;
    private Long roomNumber;
}
