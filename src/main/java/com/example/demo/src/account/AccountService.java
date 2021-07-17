package com.example.demo.src.account;



import com.example.demo.config.BaseException;
import com.example.demo.src.account.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@RequiredArgsConstructor
@Transactional
@Service
public class AccountService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AccountDao accountDao;
    private final AccountProvider accountProvider;
    private final JwtService jwtService;

    @Value("jwt.USER_INFO_PASSWORD_KEY")
    private String USER_INFO_PASSWORD_KEY;


    //POST
    public PostAccountRes createAccount(PostAccountReq postAccountReq) throws BaseException {
        //중복
        if(accountProvider.checkEmail(postAccountReq.getEmail()) ==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        String pwd;
        try{
            //암호화
            pwd = new AES128(USER_INFO_PASSWORD_KEY).encrypt(postAccountReq.getPassword());
            postAccountReq.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            long userIdx = accountDao.createAccount(postAccountReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostAccountRes(jwt,userIdx);
        } catch (Exception exception) {
            throw new BaseException(DISABLE_CREATE_JWT);
        }
    }

    public void modifyUserName(PatchAccountReq patchAccountReq) throws BaseException {
        try{
            int result = accountDao.updateAccountNickname(patchAccountReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void updateAccountStatus(PatchAccountStatusReq patchAccountStatusReq) throws BaseException {
        try{
            int result = accountDao.updateAccountStatus(patchAccountStatusReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERSTATUS);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
