package com.example.demo.src.category;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/app/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{groupName}")
    public BaseResponse<List<Category>> getCategoriesByGroupName(@PathVariable("groupName") String groupName){
        try{
            List<Category> categories = categoryService.getCategoriesByGroupName(groupName);
            return new BaseResponse<>(categories);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
