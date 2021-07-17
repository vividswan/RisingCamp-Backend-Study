package com.example.demo.src.chat.model;

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
public class GetChatRes {
    public String profileImageUrl;
    public String nickname;
    public Float mannerTemperature;
    public Integer avgResponseHour;
    public Long senderId;
    public String message;
    public String imageUrl;
    public String emoticon;
    public String sendAt;
    public Long roomNumber;
    public Boolean inquired;
}
