package com.example.demo.src.account;


import com.example.demo.config.BaseException;
import com.example.demo.src.account.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@RequiredArgsConstructor
@Transactional
@Service
public class AccountProvider {

    private final AccountDao accountDao;
    private final JwtService jwtService;

    @Value("jwt.USER_INFO_PASSWORD_KEY")
    private String USER_INFO_PASSWORD_KEY;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional(readOnly = true)
    public List<GetAccountRes> getAccounts() throws BaseException{
        try{
            List<GetAccountRes> getAccountRes = accountDao.getAccounts();
            return getAccountRes;
        }
        catch (Exception exception) {
            throw new BaseException(RESPONSE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public List<GetAccountRes> getUsersByEmail(String email) throws BaseException{
        try{
            List<GetAccountRes> getUsersRes = accountDao.getAccountByEmail(email);
            return getUsersRes;
        }
        catch (Exception exception) {
            throw new BaseException(RESPONSE_ERROR);
        }
    }


    @Transactional(readOnly = true)
    public GetAccountRes getAccountById(Long accountId) throws BaseException {
        if (accountId == null){
            accountId = jwtService.getUserIdx();
        }
        try {
            GetAccountRes getAccountRes = accountDao.getAccountById(accountId);
            return getAccountRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkEmail(String email) throws BaseException{
        try{
            return accountDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        Account account = accountDao.getPwd(postLoginReq);
        String password;
        try {
            password = new AES128(USER_INFO_PASSWORD_KEY).decrypt(account.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(postLoginReq.getPassword().equals(password)){
            long accountId = accountDao.getPwd(postLoginReq).getId();
            String jwt = jwtService.createJwt(accountId);
            return new PostLoginRes(accountId,jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

    @Transactional(readOnly = true)
    public List<GetAccountRes> getDeletedAccounts() throws BaseException{
        try{
            List<GetAccountRes> getAccountRes = accountDao.getDeletedAccounts();
            return getAccountRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
