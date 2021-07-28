package com.example.demo.src.oAuth.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.account.model.PostLoginRes;
import com.example.demo.src.oAuth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class OAuthController {

    private final OAuthService oAuthService;


    @ResponseBody
    @GetMapping("/user/kakao/jwt")
    public BaseResponse<PostLoginRes> kakaoLogin(@RequestParam(name = "code") String code){
        try{
            return new BaseResponse<>(oAuthService.kakaoLogin(code));
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @GetMapping("/kakao")
    public String kakaoLogin(){
        return "kakao.html";
    }

    @GetMapping("/user/kakao/callback")
    public String test(String code){
        return "redirect:/kakao" +"?code="+code;
    }

}
