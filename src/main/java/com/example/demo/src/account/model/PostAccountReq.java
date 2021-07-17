package com.example.demo.src.account.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostAccountReq {
    private String nickname;
    private String email;
    private String password;
    private String phoneNumber;
    private  String profileImageUrl;
}
