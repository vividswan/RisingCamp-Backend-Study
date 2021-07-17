package com.example.demo.src.chat.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.chat.model.GetChatRes;
import com.example.demo.src.chat.model.GetRoomListRes;
import com.example.demo.src.chat.model.PostChatReq;
import com.example.demo.src.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/app/chats")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/room/{roomNumber}")
    public BaseResponse<List<GetChatRes>> getChatResListByRoomNumber(@PathVariable("roomNumber") long roomNumber){
        try{
            List<GetChatRes> getChatResList = chatService.getChatResListByRoomNumber(roomNumber);
            return new BaseResponse<>(getChatResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @GetMapping("/rooms/{accountId}")
    public BaseResponse<List<GetRoomListRes>> getRoomListResByAccountId(@PathVariable("accountId") long accountId){
        try {
            List<GetRoomListRes> getRoomListRes = chatService.getRoomListResByAccountId(accountId);
            return new BaseResponse<>(getRoomListRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping("")
    public BaseResponse<Integer> createChat(@RequestBody PostChatReq postChatReq){
        try {
            int result = chatService.createChat(postChatReq);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
