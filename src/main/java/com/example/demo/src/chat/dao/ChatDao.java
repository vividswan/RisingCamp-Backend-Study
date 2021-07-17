package com.example.demo.src.chat.dao;

import com.example.demo.src.chat.model.GetChatRes;
import com.example.demo.src.chat.model.GetRoomListRes;
import com.example.demo.src.chat.model.PostChatReq;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ChatDao {

    private final JdbcTemplate jdbcTemplate;

    public List<GetChatRes> getChatResListByRoomNumber(long roomNumber){
        String getChatResByRoomNumberQuery = "SELECT \n" +
                "a.profileImageUrl, a.nickname, a.mannerTemperature, a.avgResponseHour ,\n" +
                "c.senderId, c.message, c.imageUrl, c.emoticon, \n" +
                "\n" +
                "CASE \n" +
                "WHEN DATE_FORMAT(c.createdAt, '%H') <= 12 THEN CONCAT('오전 ',DATE_FORMAT(c.createdAt, '%h:%i')) \n" +
                "ELSE CONCAT('오후 ',DATE_FORMAT(c.createdAt, '%h:%i')) \n" +
                "END AS 'sendAt', \n" +
                "\n" +
                "c.roomNumber, \n" +
                "\n" +
                "c.inquired \n" +
                "\n" +
                "FROM Post p \n" +
                "INNER JOIN Chatting c ON p.id = c.postId\n" +
                "LEFT JOIN PurchaseHistory ph ON ph.postId = p.id \n" +
                "LEFT JOIN Account a ON a.id = c.senderId AND a.id != p.accountId \n" +
                "\n" +
                "WHERE c.roomNumber = ? \n" +
                "ORDER BY c.createdAt DESC;";
        Long getChatResByRoomNumberParams = roomNumber;
        return this.jdbcTemplate.query(getChatResByRoomNumberQuery,
                (rs,rowNum) -> GetChatRes.builder()
                        .profileImageUrl(rs.getString("profileImageUrl"))
                        .nickname(rs.getString("nickname"))
                        .mannerTemperature(rs.getFloat("mannerTemperature"))
                        .avgResponseHour(rs.getInt("avgResponseHour"))
                        .senderId(rs.getLong("senderId"))
                        .message(rs.getString("message"))
                        .imageUrl(rs.getString("imageUrl"))
                        .emoticon(rs.getString("emoticon"))
                        .sendAt(rs.getString("sendAt"))
                        .roomNumber(rs.getLong("roomNumber"))
                        .inquired(rs.getBoolean("inquired")).build(),
                getChatResByRoomNumberParams);
    }

    public List<GetRoomListRes> getRoomListResByAccountId(long accountId){
        String getRoomListResByAccountIdQuery = "SELECT \n" +
                "a.profileImageUrl, a.nickname, t.townAddress, \n" +
                "DATE_FORMAT(c.createdAt, '%m월 %d일') as 'createdAt' , \n" +
                "c.message, \n" +
                "(SELECT pp.photoImageUrl FROM PostPhoto pp WHERE pp.postId = p.id AND pp.status = 'Thumbnail') as 'Thumbnail', \n" +
                "c.roomNumber \n" +
                "FROM Chatting c \n" +
                "INNER JOIN Account a ON a.id = ?  \n" +
                "INNER JOIN Post p ON p.id = c.postId \n" +
                "INNER JOIN Town t ON t.postId = p.id \n" +
                "WHERE c.createdAt in \n" +
                "(SELECT MAX(c.createdAt) FROM Chatting c \n" +
                "GROUP BY c.roomNumber) \n" +
                "ORDER BY c.createdAt DESC;";
        Long getRoomListResByAccountIdParams = accountId;

        return this.jdbcTemplate.query(getRoomListResByAccountIdQuery,
                (rs,rowNum) -> GetRoomListRes.builder()
                .profileImageUrl(rs.getString("profileImageUrl"))
                .nickname(rs.getString("nickname"))
                .townAddress(rs.getString("townAddress"))
                .createdAt(rs.getString("createdAt"))
                .message(rs.getString("message"))
                .thumbnail(rs.getString("thumbnail"))
                .roomNumber(rs.getLong("roomNumber")).build(),
                getRoomListResByAccountIdParams);
    }

    public int createChat(Long senderId, PostChatReq postChatReq){

        if (postChatReq.getRoomNumber() == null){
            String createRoomNumberQuery = "SELECT MAX(roomNumber)+1 FROM Chatting";
            postChatReq.setRoomNumber(
                    this.jdbcTemplate.queryForObject(createRoomNumberQuery, Long.class)
            );
        }


        String createSalePostQuery = "INSERT INTO Chatting (senderId, postId, message, imageUrl, emoticon, roomNumber) VALUES (?,?,?,?,?,?)";
        Object[] createSalePostParams = new Object[]{senderId, postChatReq.getPostId(), postChatReq.getMessage(), postChatReq.getImageUrl(), postChatReq.getEmoticon(), postChatReq.getRoomNumber()};
        this.jdbcTemplate.update(createSalePostQuery, createSalePostParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

}
