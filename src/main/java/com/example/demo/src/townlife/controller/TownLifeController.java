package com.example.demo.src.townlife.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.post.model.GetPostRes;
import com.example.demo.src.townlife.model.GetTownLifeRes;
import com.example.demo.src.townlife.service.TownLifeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/app/town-lifes")
public class TownLifeController {

    private final TownLifeService townLifeService;

    @GetMapping("/{townLifeId}")
    public BaseResponse<GetTownLifeRes> getTownLifeByTownLifeId(@PathVariable("townLifeId") long townLifeId){
        try{
            GetTownLifeRes getTownLifeRes = townLifeService.getTownLifeByTownLifeId(townLifeId);
            return new BaseResponse<>(getTownLifeRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
