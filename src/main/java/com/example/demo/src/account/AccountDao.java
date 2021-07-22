package com.example.demo.src.account;


import com.example.demo.src.account.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public List<GetAccountRes> getAccounts(){
        String getUsersQuery = "select * from Account where status != 'Deleted'";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetAccountRes(
                        rs.getLong("id"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("language"),
                        rs.getString("profileImageUrl"),
                        rs.getFloat("mannerTemperature"),
                        rs.getFloat("reTransactionRate"),
                        rs.getFloat("responseRate"),
                        rs.getInt("avgResponseHour")
                )
        );
    }

    public List<GetAccountRes> getAccountByEmail(String email){
        String getUsersByEmailQuery = "select * from Account where (email =? AND status != 'Deleted')";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetAccountRes(
                        rs.getLong("id"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("language"),
                        rs.getString("profileImageUrl"),
                        rs.getFloat("mannerTemperature"),
                        rs.getFloat("reTransactionRate"),
                        rs.getFloat("responseRate"),
                        rs.getInt("avgResponseHour")
                ),
                getUsersByEmailParams);
    }

    public GetAccountRes getAccountById(long accountId){
        String getUserQuery = "select * from Account where (id = ? AND status != 'Deleted')";
        long getUserParams = accountId;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetAccountRes(
                        rs.getLong("id"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("language"),
                        rs.getString("profileImageUrl"),
                        rs.getFloat("mannerTemperature"),
                        rs.getFloat("reTransactionRate"),
                        rs.getFloat("responseRate"),
                        rs.getInt("avgResponseHour")
                ),
                getUserParams);
    }

    public List<GetAccountRes> getAccountBykakaoId(long kakaoId){
        String getUserQuery = "select * from Account where (kakaoId = ? AND status != 'Deleted')";
        long getUserParams = kakaoId;
        return this.jdbcTemplate.query(getUserQuery,
                (rs, rowNum) -> new GetAccountRes(
                        rs.getLong("id"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("language"),
                        rs.getString("profileImageUrl"),
                        rs.getFloat("mannerTemperature"),
                        rs.getFloat("reTransactionRate"),
                        rs.getFloat("responseRate"),
                        rs.getInt("avgResponseHour")
                ),
                getUserParams);
    }
    

    public int createAccount(PostAccountReq postAccountReq){
        String createUserQuery = "insert into Account (nickname, email, password, phoneNumber, profileImageUrl) VALUES (?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postAccountReq.getNickname(), postAccountReq.getEmail(), postAccountReq.getPassword(), postAccountReq.getPhoneNumber(),postAccountReq.getProfileImageUrl()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from Account where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }


    public int updateAccountNickname(PatchAccountReq patchAccountReq){
        String modifyUserNameQuery = "update Account set nickname = ? where id = ? ";
        Object[] modifyUserNameParams = new Object[]{patchAccountReq.getUserName(), patchAccountReq.getAccountId()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int updateAccountStatus(PatchAccountStatusReq patchAccountStatusReq){
        String modifyUserNameQuery = "update Account set status = ? where id = ? ";
        Object[] modifyUserNameParams = new Object[]{patchAccountStatusReq.getStatus(), patchAccountStatusReq.getAccountId()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public Account getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select id, password,email, nickname from Account where (email = ? and status != 'Deleted')";
        String getPwdParams = postLoginReq.getEmail();


        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> Account.builder()
                        .id(rs.getLong("id"))
                        .password(rs.getString("password"))
                        .email(rs.getString("email"))
                        .nickname(rs.getString("nickname"))
                        .build()
                ,
                getPwdParams
                );

    }


    public List<GetAccountRes> getDeletedAccounts() {
        String getUsersQuery = "select * from Account where status = 'Deleted'";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetAccountRes(
                        rs.getLong("id"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("language"),
                        rs.getString("profileImageUrl"),
                        rs.getFloat("mannerTemperature"),
                        rs.getFloat("reTransactionRate"),
                        rs.getFloat("responseRate"),
                        rs.getInt("avgResponseHour")
                )
        );
    }

    public int createAccountKakaoId(String email, Long kakaoId) {
        String modifyUserNameQuery = "update Account set kakaoId = ? where email = ? ";
        Object[] modifyUserNameParams = new Object[]{kakaoId , email};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }
}
