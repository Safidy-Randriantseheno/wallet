package com.td2.wallet.repository;

import com.td2.wallet.model.Category;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@AllArgsConstructor
public class CategoryRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public Category findCategoryById(String categoryId) {
        String sql = "SELECT id, name, type FROM category WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{categoryId}, (resultSet, rowNum) -> {
                Category category = new Category();
                category.setId(resultSet.getString("id"));
                category.setName(resultSet.getString("name"));
                category.setType(Category.CategoryType.valueOf(resultSet.getString("type")));
                return category;
            });
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }


}
