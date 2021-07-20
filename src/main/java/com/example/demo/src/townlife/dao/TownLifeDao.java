package com.example.demo.src.townlife.dao;

import com.example.demo.src.townlife.model.GetTownLifeRes;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TownLifeDao {

    private final JdbcTemplate jdbcTemplate;

    public GetTownLifeRes getTownLifeByTownLifeId(long townLifeId) {
        String getTownLifeByTownLifeIdQuery = "SELECT \n" +
                "a.profileImageUrl, a.nickname, tl.address, \n" +
                "(SELECT MAX(t.certiCount) FROM Town t) as 'certiCount', \n" +
                "\n" +
                "CASE \n" +
                "WHEN TIMESTAMPDIFF(SECOND, tl.createdAt, NOW()) < 60 THEN concat(TIMESTAMPDIFF(SECOND, tl.createdAt, NOW()), ' 초전') \n" +
                "WHEN TIMESTAMPDIFF(MINUTE, tl.createdAt, NOW()) < 60 THEN concat(TIMESTAMPDIFF(MINUTE, tl.createdAt, NOW()), ' 분전') \n" +
                "WHEN TIMESTAMPDIFF(HOUR, tl.createdAt, NOW()) < 24 THEN concat(TIMESTAMPDIFF(HOUR, tl.createdAt, NOW()), ' 시간 전') \n" +
                "WHEN TIMESTAMPDIFF(DAY, tl.createdAt, NOW()) < 30 THEN concat(TIMESTAMPDIFF(DAY, tl.createdAt, NOW()), ' 일 전')\n" +
                "WHEN TIMESTAMPDIFF(MONTH, tl.createdAt, NOW()) < 12 THEN concat(TIMESTAMPDIFF(MONTH, tl.createdAt, NOW()), ' 개월 전')\n" +
                "ELSE concat(TIMESTAMPDIFF(YEAR, tl.createdAt, NOW()), '년 전') \n" +
                "END AS 'updatedAt', \n" +
                "\n" +
                "tl.content, tl.sympathyCount \n" +
                "\n" +
                "FROM TownLife tl \n" +
                "INNER JOIN Account a ON a.id = tl.accountId \n" +
                "INNER JOIN Town t ON (t.townAddress = tl.address AND a.id = t.accountId)\n" +
                "WHERE tl.id = ?";
        Long getTownLifeByTownLifeIdParams = townLifeId;
        return this.jdbcTemplate.queryForObject(getTownLifeByTownLifeIdQuery,
                (rs, rowNum) -> (
                        GetTownLifeRes.builder()
                        .profileImageUrl(rs.getString("profileImageUrl"))
                        .nickname(rs.getString("nickname"))
                        .address(rs.getString("address"))
                        .certiCount(rs.getInt("certiCount"))
                        .updatedAt(rs.getString("updatedAt"))
                        .content(rs.getString("content"))
                        .sympathyCount(rs.getInt("sympathyCount"))
                        .build()) ,
                getTownLifeByTownLifeIdParams);
    }
}
