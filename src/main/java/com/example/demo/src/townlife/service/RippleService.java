package com.example.demo.src.townlife.service;

import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.GetPostRes;
import com.example.demo.src.townlife.dao.RippleDao;
import com.example.demo.src.townlife.model.GetRippleRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@Transactional
@Service
public class RippleService {

    private final RippleDao rippleDao;

    @Transactional(readOnly = true)
    public List<GetRippleRes> getRippleResListByTownLifeId(long townLifeId) throws BaseException{
        try {
            List<GetRippleRes> getRippleResList = rippleDao.getRippleResListByTownLifeId(townLifeId);
            return getRippleResList;
        } catch (Exception exception) {
            throw new BaseException(INVALID_TOWNLIFES_ID);
        }
    }

    @Transactional(readOnly = true)
    public List<GetRippleRes> getNestedRippleListByRippleId(long rippleId) throws BaseException{
        try {
            List<GetRippleRes> nestedRippleList = rippleDao.getNestedRippleListByRippleId(rippleId);
            return nestedRippleList;
        } catch (Exception exception) {
            throw new BaseException(INVALID_RIPPLES_ID);
        }
    }
}
