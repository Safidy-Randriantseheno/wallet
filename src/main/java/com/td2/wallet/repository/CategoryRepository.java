package com.td2.wallet.repository;

import com.td2.wallet.model.Category;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class CategoryRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public Category findCategoryById(String categoryId) {
        String sql = "SELECT * FROM category WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{categoryId},
                (rs, rowNum) -> new Category(rs.getString("id"), rs.getString("name"), Category.CategoryType.valueOf(rs.getString("type"))));
    }
}
