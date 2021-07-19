package com.example.demo.src.post.dao;

import com.example.demo.src.post.model.GetPostPhotoRes;
import com.example.demo.src.post.model.GetPostRes;
import com.example.demo.src.post.model.GetSalePostRes;
import com.example.demo.src.post.model.PostSalePostReq;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PostDao {

    private final JdbcTemplate jdbcTemplate;


    public List<GetSalePostRes> getCompletedPostsByAccountId(long accountId) {
        String getCompletedPostsByAccountIdQuery = "SELECT \n" +
                "pp.photoImageUrl, \n" +
                "p.title, t.townAddress 'address', \n" +
                "\n" +
                "CASE \n" +
                "WHEN p.raisedCount > 0 THEN '끌올' \n" +
                "ELSE '끌올 x' \n" +
                "END as 'isRaised', \n" +
                "\n" +
                "CASE \n" +
                "WHEN TIMESTAMPDIFF(SECOND, p.updatedAt, NOW()) < 60 THEN concat(TIMESTAMPDIFF(SECOND, p.updatedAt, NOW()), ' 초전') \n" +
                "WHEN TIMESTAMPDIFF(MINUTE, p.updatedAt, NOW()) < 60 THEN concat(TIMESTAMPDIFF(MINUTE, p.updatedAt, NOW()), ' 분전') \n" +
                "WHEN TIMESTAMPDIFF(HOUR, p.updatedAt, NOW()) < 24 THEN concat(TIMESTAMPDIFF(HOUR, p.updatedAt, NOW()), ' 시간 전') \n" +
                "WHEN TIMESTAMPDIFF(DAY, p.updatedAt, NOW()) < 30 THEN concat(TIMESTAMPDIFF(DAY, p.updatedAt, NOW()), ' 일 전')\n" +
                "WHEN TIMESTAMPDIFF(MONTH, p.updatedAt, NOW()) < 12 THEN concat(TIMESTAMPDIFF(MONTH, p.updatedAt, NOW()), ' 개월 전')\n" +
                "ELSE concat(TIMESTAMPDIFF(YEAR, p.updatedAt, NOW()), '년 전') \n" +
                "END AS 'updatedAt', \n" +
                "\n" +
                "p.raisedCount, p.saleStatus, p.salePrice, \n" +
                "\n" +
                "CASE \n" +
                "WHEN (SELECT COUNT(DISTINCT(c.senderId))-1 FROM Chatting c WHERE c.postId = p.id) = -1 THEN 0 \n" +
                "WHEN (SELECT COUNT(DISTINCT(c.senderId)) FROM Chatting c WHERE c.postId = p.id) = 1 THEN 1 \n" +
                "ELSE (SELECT COUNT(DISTINCT(c.senderId))-1 FROM Chatting c WHERE c.postId = p.id) \n" +
                "END AS 'ChatCount', \n" +
                "\n" +
                "p.likeCount, \n" +
                "(ph.id) 'reviewId' \n" +
                "\n" +
                "FROM Post p \n" +
                "LEFT JOIN PostPhoto pp ON (pp.postId = p.id) AND (pp.status = 'Thumbnail') \n" +
                "LEFT JOIN PurchaseHistory ph ON ph.postId = p.id \n" +
                "INNER JOIN Town t ON t.postId = p.id \n" +
                "\n" +
                "WHERE (p.accountId = ?) AND (p.saleStatus = 'Completed')";
        Long getCompletedPostsByAccountIdParams = accountId;
        return this.jdbcTemplate.query(getCompletedPostsByAccountIdQuery,
                (rs, rowNum) -> GetSalePostRes.builder()
                        .photoImageUrl(rs.getString("photoImageUrl"))
                        .title(rs.getString("title"))
                        .address(rs.getString("address"))
                        .isRaised(rs.getString("isRaised"))
                        .updatedAt(rs.getString("updatedAt"))
                        .raisedCount(rs.getInt("raisedCount"))
                        .saleStatus(rs.getString("saleStatus"))
                        .salePrice(rs.getInt("salePrice"))
                        .chatCount(rs.getInt("chatCount"))
                        .likeCount(rs.getInt("likeCount")).build(),
                getCompletedPostsByAccountIdParams);
    }

    public List<GetSalePostRes> getSalePosts(long accountId) {
        String getSalePostQuery = "SELECT \n" +
                "t.detailAddress as 'accountAddress', \n" +
                "pp.photoImageUrl, p.title, t2.townAddress, p.salePrice, \n" +
                "\n" +
                "CASE \n" +
                "WHEN p.raisedCount > 0 THEN '끌올' \n" +
                "ELSE '끌올 x' \n" +
                "END as 'isRaised', \n" +
                "\n" +
                "CASE \n" +
                "WHEN TIMESTAMPDIFF(SECOND, p.updatedAt, NOW()) < 60 THEN concat(TIMESTAMPDIFF(SECOND, p.updatedAt, NOW()), ' 초전') \n" +
                "WHEN TIMESTAMPDIFF(MINUTE, p.updatedAt, NOW()) < 60 THEN concat(TIMESTAMPDIFF(MINUTE, p.updatedAt, NOW()), ' 분전') \n" +
                "WHEN TIMESTAMPDIFF(HOUR, p.updatedAt, NOW()) < 24 THEN concat(TIMESTAMPDIFF(HOUR, p.updatedAt, NOW()), ' 시간 전') \n" +
                "WHEN TIMESTAMPDIFF(DAY, p.updatedAt, NOW()) < 30 THEN concat(TIMESTAMPDIFF(DAY, p.updatedAt, NOW()), ' 일 전')\n" +
                "WHEN TIMESTAMPDIFF(MONTH, p.updatedAt, NOW()) < 12 THEN concat(TIMESTAMPDIFF(MONTH, p.updatedAt, NOW()), ' 개월 전')\n" +
                "ELSE concat(TIMESTAMPDIFF(YEAR, p.updatedAt, NOW()), '년 전') \n" +
                "END AS 'updatedAt', \n" +
                "\n" +
                "CASE \n" +
                "WHEN (SELECT COUNT(DISTINCT(c.senderId))-1 FROM Chatting c WHERE c.postId = p.id) = -1 THEN 0 \n" +
                "WHEN (SELECT COUNT(DISTINCT(c.senderId)) FROM Chatting c WHERE c.postId = p.id) = 1 THEN 1 \n" +
                "ELSE (SELECT COUNT(DISTINCT(c.senderId))-1 FROM Chatting c WHERE c.postId = p.id) \n" +
                "END AS 'ChatCount', \n" +
                "\n" +
                "p.likeCount \n" +
                "\n" +
                "FROM Post p \n" +
                "LEFT JOIN PostPhoto pp ON (pp.postId = p.id) AND (pp.status = 'Thumbnail') \n" +
                "JOIN Town t \n" +
                "INNER JOIN Town t2 ON t2.postId = p.id \n" +
                "WHERE (t.status = 'Active') AND (t.accountId = ?) \n" +
                "AND (p.status = 'SalePost') AND (p.saleStatus = 'Sale')\n" +
                "AND ((SELECT MIN(addressRange) FROM TownRange WHERE (\n" +
                "TownRange.townAddress = (SELECT townAddress FROM Town WHERE Town.postId = p.id) \n" +
                "AND \n" +
                "TownRange.phase = (SELECT phase FROM Town WHERE Town.postId = p.id))\n" +
                ") IN \n" +
                "(SELECT addressRange FROM TownRange WHERE (TownRange.townAddress = \n" +
                "(SELECT townAddress FROM Town WHERE Town.accountId = ?) \n" +
                "AND \n" +
                "TownRange.phase = (SELECT phase FROM Town WHERE Town.accountId = ?))\n" +
                ")) \n" +
                "ORDER BY p.updatedAt DESC";
        Long getSalePostsParams = accountId;
        return this.jdbcTemplate.query(getSalePostQuery,
                (rs, rowNum) -> GetSalePostRes.builder()
                        .photoImageUrl(rs.getString("photoImageUrl"))
                        .title(rs.getString("title"))
                        .townAddress(rs.getString("townAddress"))
                        .salePrice(rs.getInt("salePrice"))
                        .isRaised(rs.getString("isRaised"))
                        .updatedAt(rs.getString("updatedAt"))
                        .chatCount(rs.getInt("chatCount"))
                        .likeCount(rs.getInt("likeCount")).build(),
                getSalePostsParams, getSalePostsParams, getSalePostsParams);
    }

    public List<GetPostPhotoRes> getPostPhotosByPostId(long postId) {
        String getPostPhotosByPostIdQuery = "SELECT pp.photoImageUrl, pp.status  \n" +
                "FROM PostPhoto pp \n" +
                "INNER JOIN Post p ON p.id = pp.postId \n" +
                "WHERE p.id = ? \n" +
                "ORDER BY pp.status DESC;";
        Long getPostPhotosByPostIdParams = postId;
        return this.jdbcTemplate.query(getPostPhotosByPostIdQuery,
                (rs, rowNum) -> new GetPostPhotoRes(
                        rs.getString("photoImageUrl"),
                        rs.getString("status")
                ),
                getPostPhotosByPostIdParams);
    }

    public GetPostRes getPostByPostId(long postId) {
        String getPostByPostIdQuery = "SELECT \n" +
                "a.nickname, t.townAddress, a.mannerTemperature, p.saleStatus, \n" +
                "p.title, c.name as category,  \n" +
                "\n" +
                "CASE \n" +
                "WHEN p.raisedCount > 0 THEN '끌올' \n" +
                "ELSE '끌올 x' \n" +
                "END as 'isRaised', \n" +
                "\n" +
                "CASE \n" +
                "WHEN TIMESTAMPDIFF(SECOND, p.updatedAt, NOW()) < 60 THEN concat(TIMESTAMPDIFF(SECOND, p.updatedAt, NOW()), ' 초전') \n" +
                "WHEN TIMESTAMPDIFF(MINUTE, p.updatedAt, NOW()) < 60 THEN concat(TIMESTAMPDIFF(MINUTE, p.updatedAt, NOW()), ' 분전') \n" +
                "WHEN TIMESTAMPDIFF(HOUR, p.updatedAt, NOW()) < 24 THEN concat(TIMESTAMPDIFF(HOUR, p.updatedAt, NOW()), ' 시간 전') \n" +
                "WHEN TIMESTAMPDIFF(DAY, p.updatedAt, NOW()) < 30 THEN concat(TIMESTAMPDIFF(DAY, p.updatedAt, NOW()), ' 일 전')\n" +
                "WHEN TIMESTAMPDIFF(MONTH, p.updatedAt, NOW()) < 12 THEN concat(TIMESTAMPDIFF(MONTH, p.updatedAt, NOW()), ' 개월 전')\n" +
                "ELSE concat(TIMESTAMPDIFF(YEAR, p.updatedAt, NOW()), '년 전') \n" +
                "END AS 'updatedAt', \n" +
                "\n" +
                "p.content, p.likeCount, p.viewCount, \n" +
                "\n" +
                "CASE\n" +
                "WHEN (SELECT likeCheck FROM AccountPostRelation ap WHERE (ap.accountId = 3 AND ap.postId = p.id)) = 1 THEN 1 \n" +
                "ELSE 0 \n" +
                "END AS 'likeCheck', \n" +
                "\n" +
                "p.salePrice, p.priceSuggestion \n" +
                "\n" +
                "FROM Post p \n" +
                "INNER JOIN Account a ON a.id = p.accountId \n" +
                "INNER JOIN Town t ON t.postId = p.id \n" +
                "INNER JOIN Category c ON p.categoryId = c.id  \n" +
                "Where p.id = ?";
        Long getPostByPostIdParams = postId;
        return this.jdbcTemplate.queryForObject(getPostByPostIdQuery,
                (rs, rowNum) -> GetPostRes.builder()
                        .nickname(rs.getString("nickname"))
                        .townAddress(rs.getString("townAddress"))
                        .mannerTemperature(rs.getFloat("mannerTemperature"))
                        .saleStatus(rs.getString("saleStatus"))
                        .title(rs.getString("title"))
                        .category(rs.getString("category"))
                        .isRaised(rs.getString("isRaised"))
                        .updatedAt(rs.getString("updatedAt"))
                        .content(rs.getString("content"))
                        .likeCount(rs.getInt("likeCount"))
                        .viewCount(rs.getInt("viewCount"))
                        .likeCheck(rs.getBoolean("likeCheck"))
                        .salePrice(rs.getInt("salePrice"))
                        .priceSuggestion(rs.getBoolean("priceSuggestion"))
                        .build(),
                getPostByPostIdParams);
    }


    public int createSalePost(PostSalePostReq postSalePostReq, Integer categotyId, Long accountId) {
        String createSalePostQuery = "INSERT INTO Post (status, accountId, title, content, categoryId, salePrice,priceSuggestion) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] createSalePostParams = new Object[]{"SalePost", accountId, postSalePostReq.getTitle(), postSalePostReq.getContent(), categotyId, postSalePostReq.getSalePrice(), postSalePostReq.getPriceSuggestion()};
        this.jdbcTemplate.update(createSalePostQuery, createSalePostParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery, int.class);
    }

    public void updatePostStatus(Long postId, String status) {
        String updatePostStatusQuery = "UPDATE Post SET saleStatus = ? WHERE (id =" + postId + ")";
        String updatePostStatusParams = status;
        jdbcTemplate.update(updatePostStatusQuery, updatePostStatusParams);
    }

}
