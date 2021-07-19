package com.example.demo.src.post.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.account.model.Account;
import com.example.demo.src.account.model.PatchAccountStatusReq;
import com.example.demo.src.post.model.GetPostPhotoRes;
import com.example.demo.src.post.model.GetPostRes;
import com.example.demo.src.post.model.GetSalePostRes;
import com.example.demo.src.post.model.PostSalePostReq;
import com.example.demo.src.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/app/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/account/{accountId}/completed-posts")
    public BaseResponse<List<GetSalePostRes>> getCompletedPostsByAccountId(@PathVariable("accountId") long accountId) {
        try{
            List<GetSalePostRes> getSalePostRes = postService.getCompletedPostsByAccountId(accountId);
            return new BaseResponse<>(getSalePostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @GetMapping("/sale-posts/account/{accountId}")
    public BaseResponse<List<GetSalePostRes>> getSalePosts(@PathVariable("accountId") long accountId) {
        try{
            List<GetSalePostRes> getSalePostResList = postService.getSalePosts(accountId);
            return new BaseResponse<>(getSalePostResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @GetMapping("/photos/{postId}")
    public BaseResponse<List<GetPostPhotoRes>> getPostPhotosByPostId(@PathVariable("postId") long postId){
        try{
            List<GetPostPhotoRes> getPostPhotoResList = postService.getPostPhotosByPostId(postId);
            return new BaseResponse<>(getPostPhotoResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @GetMapping("{postId}")
    public BaseResponse<GetPostRes> getPostByPostId(@PathVariable("postId") long postId){
        try{
            GetPostRes getPostRes = postService.getPostByPostId(postId);
            return new BaseResponse<>(getPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping("/sale-post")
    public BaseResponse<Integer> createSalePost(@RequestBody PostSalePostReq postSalePostReq){
        try{
            Integer result = postService.createSalePost(postSalePostReq);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/status")
    public BaseResponse<String> updatePostStatus(@RequestParam Long postId, @RequestParam String status){
        try {
            postService.updatePostStatus(postId, status);
            String result = postId+"번 Post의 Status가 "+status+"로 변경되었습니다.";
            System.out.println(result);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
