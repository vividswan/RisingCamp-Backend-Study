package com.example.demo.src.post.service;

import com.example.demo.config.BaseException;
import com.example.demo.src.account.AccountDao;
import com.example.demo.src.category.CategoryService;
import com.example.demo.src.post.dao.PostDao;
import com.example.demo.src.post.model.GetPostPhotoRes;
import com.example.demo.src.post.model.GetPostRes;
import com.example.demo.src.post.model.GetSalePostRes;
import com.example.demo.src.post.model.PostSalePostReq;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Transactional
@RequiredArgsConstructor
@Service
public class PostService {

    private final AccountDao accountDao;
    private final PostDao postDao;
    private final CategoryService categoryService;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public List<GetSalePostRes> getCompletedPostsByAccountId(long accountId) throws BaseException{
        try {
            accountDao.getAccountById(accountId);
        } catch (Exception exception) {
            throw new BaseException(INVALID_ACCOUNT_ID);
        }

        try {
            List<GetSalePostRes> getSalePostResList = postDao.getCompletedPostsByAccountId(accountId);
            return getSalePostResList;
        } catch (Exception exception) {
            throw new BaseException(USERS_EMPTY_USER_ID);
        }
    }

    @Transactional(readOnly = true)
    public List<GetSalePostRes> getSalePosts(long accountId) throws BaseException{

        try {
            List<GetSalePostRes> getSalePostResList = postDao.getSalePosts(accountId);
            return getSalePostResList;
        } catch (Exception exception) {
            throw new BaseException(USERS_EMPTY_USER_ID);
        }
    }

    @Transactional(readOnly = true)
    public List<GetPostPhotoRes> getPostPhotosByPostId(long postId) throws BaseException{
        try {
            List<GetPostPhotoRes> getPostPhotoResList = postDao.getPostPhotosByPostId(postId);
            return getPostPhotoResList;
        } catch (Exception exception) {
            throw new BaseException(INVALID_POSTS_ID);
        }
    }

    @Transactional(readOnly = true)
    public GetPostRes getPostByPostId(long postId) throws BaseException{
        try {
            GetPostRes getPostRes = postDao.getPostByPostId(postId);
            return getPostRes;
        } catch (Exception exception) {
            throw new BaseException(INVALID_POSTS_ID);
        }
    }

    public Integer createSalePost(PostSalePostReq postSalePostReq) throws BaseException {
        int categoryId = categoryService.getCategoryByCategoryName(postSalePostReq.getCategory()).getId();
        Long accountId = jwtService.getUserIdx();
        try {
            Integer result = postDao.createSalePost(postSalePostReq, categoryId, accountId);
            return result;
        } catch (Exception exception) {
            throw new BaseException(INVALID_POSTS_INFO);
        }
    }

    public void updatePostStatus(Long postId, String status) throws BaseException{
        try {
             postDao.updatePostStatus(postId, status);
        } catch (Exception exception) {
            throw new BaseException(INVALID_POSTS_INFO);
        }
    }
}
