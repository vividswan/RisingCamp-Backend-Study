package com.example.demo.src.account.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAccountRes {
    private Long id;
    private String nickname;
    private String email;
    private String password;
    private String language;
    private String profileUrl;
    private Float mannerTemperature;
    private Float reTransactionRate;
    private Float responseRate;
    private Integer avgResponseHour;
}
