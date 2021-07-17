package com.example.demo.src.chat.service;

import com.example.demo.config.BaseException;
import com.example.demo.src.chat.dao.ChatDao;
import com.example.demo.src.chat.model.GetChatRes;
import com.example.demo.src.chat.model.GetRoomListRes;
import com.example.demo.src.chat.model.PostChatReq;
import com.example.demo.src.post.model.GetPostPhotoRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatService {

    private final ChatDao chatDao;
    private final JwtService jwtService;


    @Transactional(readOnly = true)
    public List<GetChatRes> getChatResListByRoomNumber(long roomNumber) throws BaseException {
        try {
            List<GetChatRes> getChatResList = chatDao.getChatResListByRoomNumber(roomNumber);
            return getChatResList;
        } catch (Exception exception) {
            throw new BaseException(INVALID_ROOMNUBER_ID);
        }
    }

    @Transactional(readOnly = true)
    public List<GetRoomListRes> getRoomListResByAccountId(long accountId) throws BaseException{
        try {
            List<GetRoomListRes> getRoomListRes = chatDao.getRoomListResByAccountId(accountId);
            return getRoomListRes;
        } catch (Exception exception) {
            throw new BaseException(INVALID_ACCOUNT_ID);
        }
    }

    public int createChat(PostChatReq postChatReq) throws BaseException{
        Long accountId = jwtService.getUserIdx();
        try {
            int result = chatDao.createChat(accountId, postChatReq);
            return result;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new BaseException(INVALID_POSTS_ID);
        }
    }
}
