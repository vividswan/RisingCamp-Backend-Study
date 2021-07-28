package com.example.demo.src.oAuth.service;

import com.example.demo.config.BaseException;
import com.example.demo.src.account.AccountProvider;
import com.example.demo.src.account.AccountService;
import com.example.demo.src.account.model.*;
import com.example.demo.src.oAuth.config.KakaoOAuth2;
import com.example.demo.src.oAuth.model.KakaoUserInfo;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR;

@Transactional
@RequiredArgsConstructor
@Service
public class OAuthService {

    private final KakaoOAuth2 kakaoOAuth2;
    private final AccountProvider accountProvider;
    private final AccountService accountService;
    private final JwtService jwtService;

    @Value("jwt.USER_INFO_PASSWORD_KEY")
    private String USER_INFO_PASSWORD_KEY;

    @Value("oAuth.KAKAO_TOKEN")
    private String KAKAO_TOKEN;

    public PostLoginRes kakaoLogin(String code) throws BaseException {
        KakaoUserInfo kakaoUserInfo = kakaoOAuth2.getUserInfo(code);
        Long kakaoId = kakaoUserInfo.getId();
        String nickname = kakaoUserInfo.getNickname();
        String email = kakaoUserInfo.getEmail();

        List<GetAccountRes> account = accountProvider.getAccountByKakaoId(kakaoId);
        if(account.size() == 0){
            List<GetAccountRes> emailAccount = accountProvider.getUsersByEmail(email);
            if (emailAccount.size() >0){
                accountService.createAccountKakaoId(email, kakaoId);
            }
            else{
                PostAccountReq postAccountReq = new PostAccountReq(nickname, email, kakaoId + KAKAO_TOKEN, null, null);
                accountService.createAccount(postAccountReq);
                accountService.createAccountKakaoId(email, kakaoId);
            }
        }
        try{
            Long accountId = accountProvider.getAccountByKakaoId(kakaoId).get(0).getId();
            String jwt = jwtService.createJwt(accountId);
            return new PostLoginRes(accountId,jwt);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }


    }
}
