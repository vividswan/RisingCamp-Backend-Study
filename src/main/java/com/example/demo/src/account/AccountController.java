package com.example.demo.src.account;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.account.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/app/accounts")
public class AccountController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AccountProvider accountProvider;
    private final AccountService accountService;
    private final JwtService jwtService;



    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/accounts
    public BaseResponse<List<GetAccountRes>> getAccounts(@RequestParam(required = false) String email) {
        try{
            if(email == null){
                List<GetAccountRes> getUsersRes = accountProvider.getAccounts();
                return new BaseResponse<>(getUsersRes);
            }
            // Get Users
            List<GetAccountRes> getUsersRes = accountProvider.getUsersByEmail(email);
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/deleted-status") // (GET) 127.0.0.1:9000/app/accounts/deleted-status
    public BaseResponse<List<GetAccountRes>> getDeletedAccounts() {
        try{
            // Get Users
            List<GetAccountRes> getUsersRes = accountProvider.getDeletedAccounts();
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 1명 조회 API
     * [GET] /users/:userIdx
     * @return BaseResponse<GetAccountRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{accountId}") // (GET) 127.0.0.1:9000/app/accounts/:accountId
    public BaseResponse<GetAccountRes> getAccountById(@PathVariable("accountId") Long accountId) {
        // Get Users
        try{
            GetAccountRes getAccountRes = accountProvider.getAccountById(accountId);
            return new BaseResponse<>(getAccountRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostAccountRes> createAccount(@RequestBody PostAccountReq postAccountReq) {
        if(postAccountReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현
        if(!isRegexEmail(postAccountReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        //전화번호 정규표현
        if(!isRegexPhoneNumber(postAccountReq.getPhoneNumber())){
            return new BaseResponse<>(POST_USERS_INVALID_PHONENUMBER);
        }

        //비밀번호 정규표현
        if(!isRegexPassword(postAccountReq.getPassword())){
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }

        try{
            PostAccountRes postAccountRes = accountService.createAccount(postAccountReq);
            return new BaseResponse<>(postAccountRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 로그인 API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        try{

            //이메일 정규표현
            if(!isRegexEmail(postLoginReq.getEmail())){
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
            }

            //비밀번호 정규표현
            if(!isRegexPassword(postLoginReq.getPassword())){
                return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
            }

            PostLoginRes postLoginRes = accountProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저정보변경 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{accountId}")
    public BaseResponse<String> updateAccountNickname(@PathVariable("accountId") Long accountId, @RequestBody Account account){
        try {
            //jwt에서 idx 추출.
            long userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(accountId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경
            PatchAccountReq patchAccountReq = new PatchAccountReq(accountId,account.getNickname());
            accountService.modifyUserName(patchAccountReq);

            String result = "";
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/status/{accountId}")
    public BaseResponse<String> updateAccountStatus(@PathVariable("accountId") Long accountId, @RequestBody Account account){
        try {
            //jwt에서 idx 추출.
            long userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(accountId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경
            PatchAccountStatusReq patchAccountStatusReq = new PatchAccountStatusReq(account.getId(), account.getStatus());
            accountService.updateAccountStatus(patchAccountStatusReq);

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @GetMapping("/authentication")
    public BaseResponse<GetAccountRes> getAuthenticatedAccount(){
        try{
            GetAccountRes getAccountRes = accountProvider.getAccountById(null);
            return new BaseResponse<>(getAccountRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
