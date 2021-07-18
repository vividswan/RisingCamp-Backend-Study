package com.example.demo.src.category;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CategoryDao {

    private final JdbcTemplate jdbcTemplate;

    public Category getCategoryByCategoryName(String categoryName){
        String getCategoryByCategoryNameQuery = "SELECT * \n" +
                "FROM Category c " +
                "WHERE c.name = ?";
        String getCategoryByCategoryNameParams = categoryName;

        return this.jdbcTemplate.queryForObject(getCategoryByCategoryNameQuery,
                (rs, rowNum) -> Category.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .imageUrl(rs.getString("imageUrl"))
                        .group(rs.getString("group"))
                        .build(),
         getCategoryByCategoryNameParams);
    }

    public List<Category> getCategoriesByGroupName(String groupName) {
        String getCategoriesByGroupNameQuery = "SELECT * \n" +
                "FROM Category c " +
                "WHERE c.group = ?";
        String getCategoriesByGroupNameParams = groupName;

        return this.jdbcTemplate.query(getCategoriesByGroupNameQuery,
                (rs, rowNum) -> Category.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .imageUrl(rs.getString("imageUrl"))
                        .group(rs.getString("group"))
                        .build(),
                getCategoriesByGroupNameParams);
        }
}
