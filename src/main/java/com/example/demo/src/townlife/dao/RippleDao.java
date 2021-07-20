package com.example.demo.src.townlife.dao;

import com.example.demo.src.townlife.model.GetRippleRes;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class RippleDao {

    private final JdbcTemplate jdbcTemplate;

    public List<GetRippleRes> getRippleResListByTownLifeId(long townLifeId) {
        String getRippleResByTownLifeIdQuery = "SELECT \n" +
                "a.profileImageUrl, a.nickname, t.townAddress, \n" +
                "\n" +
                "CASE \n" +
                "WHEN TIMESTAMPDIFF(SECOND, r.updatedAt, NOW()) < 60 THEN concat(TIMESTAMPDIFF(SECOND, r.updatedAt, NOW()), ' 초전') \n" +
                "WHEN TIMESTAMPDIFF(MINUTE, r.updatedAt, NOW()) < 60 THEN concat(TIMESTAMPDIFF(MINUTE, r.updatedAt, NOW()), ' 분전') \n" +
                "WHEN TIMESTAMPDIFF(HOUR, r.updatedAt, NOW()) < 24 THEN concat(TIMESTAMPDIFF(HOUR, r.updatedAt, NOW()), ' 시간 전') \n" +
                "WHEN TIMESTAMPDIFF(DAY, r.updatedAt, NOW()) < 30 THEN concat(TIMESTAMPDIFF(DAY, r.updatedAt, NOW()), ' 일 전')\n" +
                "WHEN TIMESTAMPDIFF(MONTH, r.updatedAt, NOW()) < 12 THEN concat(TIMESTAMPDIFF(MONTH, r.updatedAt, NOW()), ' 개월 전')\n" +
                "ELSE concat(TIMESTAMPDIFF(YEAR, r.updatedAt, NOW()), '년 전') \n" +
                "END AS 'updatedAt', \n" +
                "\n" +
                "r.content, r.likeView, \n" +
                "\n" +
                "(SELECT COUNT(*) FROM Ripple nr WHERE nr.isNested = true AND nr.parentRippleId = r.id) as 'nestedCount',\n" +
                "CASE \n" +
                "WHEN r.accountId = r.townLifeId THEN 1 \n" +
                "ELSE 0 \n" +
                "END AS 'isWriter'"+
                "\n" +
                "FROM Ripple r \n" +
                "INNER JOIN Account a ON a.id = r.accountId \n" +
                "INNER JOIN Town t ON t.accountId = a.id \n" +
                "WHERE (r.townLifeId = ? AND r.isNested = false) \n" +
                "ORDER BY r.updatedAt DESC;";
        Long getRippleResByTownLifeIdParams = townLifeId;
        return this.jdbcTemplate.query(getRippleResByTownLifeIdQuery,
                (rs,rowNum) ->  GetRippleRes.builder()
                        .profileImageUrl(rs.getString("profileImageUrl"))
                        .nickname(rs.getString("nickname"))
                        .townAddress(rs.getString("townAddress"))
                        .updatedAt(rs.getString("updatedAt"))
                        .content(rs.getString("content"))
                        .likeView(rs.getInt("likeView"))
                        .nestedCount(rs.getInt("nestedCount"))
                        .isWriter(rs.getBoolean("isWriter"))
                        .build(),
                    getRippleResByTownLifeIdParams);
    }

    public List<GetRippleRes> getNestedRippleListByRippleId(long rippleId) {
        String getNestedRippleListByRippleIdQuery = "SELECT \n" +
                "a.profileImageUrl, a.nickname, t.townAddress, \n" +
                "\n" +
                "CASE \n" +
                "WHEN TIMESTAMPDIFF(SECOND, r.updatedAt, NOW()) < 60 THEN concat(TIMESTAMPDIFF(SECOND, r.updatedAt, NOW()), ' 초전') \n" +
                "WHEN TIMESTAMPDIFF(MINUTE, r.updatedAt, NOW()) < 60 THEN concat(TIMESTAMPDIFF(MINUTE, r.updatedAt, NOW()), ' 분전') \n" +
                "WHEN TIMESTAMPDIFF(HOUR, r.updatedAt, NOW()) < 24 THEN concat(TIMESTAMPDIFF(HOUR, r.updatedAt, NOW()), ' 시간 전') \n" +
                "WHEN TIMESTAMPDIFF(DAY, r.updatedAt, NOW()) < 30 THEN concat(TIMESTAMPDIFF(DAY, r.updatedAt, NOW()), ' 일 전')\n" +
                "WHEN TIMESTAMPDIFF(MONTH, r.updatedAt, NOW()) < 12 THEN concat(TIMESTAMPDIFF(MONTH, r.updatedAt, NOW()), ' 개월 전')\n" +
                "ELSE concat(TIMESTAMPDIFF(YEAR, r.updatedAt, NOW()), '년 전') \n" +
                "END AS 'updatedAt', \n" +
                "\n" +
                "r.content, r.likeView, \n" +
                "\n" +
                "CASE \n" +
                "WHEN r.accountId = tl.id THEN 1 \n" +
                "ELSE 0 \n" +
                "END AS 'isWriter'\n" +
                "\n" +
                "FROM Ripple r \n" +
                "INNER JOIN Account a ON a.id = r.accountId \n" +
                "INNER JOIN Town t ON t.accountId = a.id \n" +
                "INNER JOIN TownLife tl ON tl.id = r.townLifeId \n" +
                "WHERE (r.isNested = true AND r.parentRippleId = ?) \n" +
                "ORDER BY r.updatedAt ASC;";
        Long getNestedRippleListByRippleIdParam = rippleId;

        return this.jdbcTemplate.query(getNestedRippleListByRippleIdQuery,
                (rs,rowNum) ->  GetRippleRes.builder()
                        .profileImageUrl(rs.getString("profileImageUrl"))
                .nickname(rs.getString("nickname"))
                .townAddress(rs.getString("townAddress"))
                .updatedAt(rs.getString("updatedAt"))
                .content(rs.getString("content"))
                .likeView(rs.getInt("likeView"))
                .isWriter(rs.getBoolean("isWriter"))
                .build(),
                getNestedRippleListByRippleIdParam
                );
    }
}
