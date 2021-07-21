package com.example.demo.src.townlife.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.townlife.model.GetRippleRes;
import com.example.demo.src.townlife.service.RippleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/app/ripples")
public class RippleController {

    private final RippleService rippleService;

    @GetMapping("/{townLifeId}")
    public BaseResponse<List<GetRippleRes>> getRippleResListByTownLifeId(@PathVariable("townLifeId") long townLifeId){
        try{
            List<GetRippleRes> getRippleResList = rippleService.getRippleResListByTownLifeId(townLifeId);
            return new BaseResponse<>(getRippleResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @GetMapping("/nested-ripples/{rippleId}")
    public BaseResponse<List<GetRippleRes>> getNestedRippleListByRippleId(@PathVariable("rippleId") long rippleId){
        try{
            List<GetRippleRes> nestedRippleList = rippleService.getNestedRippleListByRippleId(rippleId);
            return new BaseResponse<>(nestedRippleList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
