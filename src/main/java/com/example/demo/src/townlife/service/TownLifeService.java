package com.example.demo.src.townlife.service;

import com.example.demo.config.BaseException;
import com.example.demo.src.townlife.dao.TownLifeDao;
import com.example.demo.src.townlife.model.GetTownLifeRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.INVALID_POSTS_ID;
import static com.example.demo.config.BaseResponseStatus.INVALID_TOWNLIFES_ID;

@Transactional
@RequiredArgsConstructor
@Service
public class TownLifeService {

    private final TownLifeDao townLifeDao;

    public GetTownLifeRes getTownLifeByTownLifeId(long townLifeId) throws BaseException{
        try {
            GetTownLifeRes townLifeRes = townLifeDao.getTownLifeByTownLifeId(townLifeId);
            return townLifeRes;
        } catch (Exception exception) {
            throw new BaseException(INVALID_TOWNLIFES_ID);
        }
    }
}
