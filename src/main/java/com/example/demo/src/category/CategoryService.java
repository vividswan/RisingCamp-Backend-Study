package com.example.demo.src.category;

import com.example.demo.config.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_CATEGORY_NAME;
import static com.example.demo.config.BaseResponseStatus.INVALID_GROUP_NAME;

@Transactional
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryDao categoryDao;

    @Transactional(readOnly = true)
    public Category getCategoryByCategoryName(String categoryName) throws BaseException{
        try {
            Category category = categoryDao.getCategoryByCategoryName(categoryName);
            return category;
        } catch (Exception exception) {
            throw new BaseException(INVALID_CATEGORY_NAME);
        }

    }


    @Transactional(readOnly = true)
    public List<Category> getCategoriesByGroupName(String groupName) throws BaseException{
        try {
            List<Category> categories = categoryDao.getCategoriesByGroupName(groupName);
            return categories;
        } catch (Exception exception) {
            throw new BaseException(INVALID_GROUP_NAME);
        }
    }
}
