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
        String sql = "SELECT * FROM category WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{categoryId}, categoryResultSetExtractor);
    }

    private final ResultSetExtractor<Category> categoryResultSetExtractor = resultSet -> {
        if (resultSet.next()) {
            return mapResultSetToCategory(resultSet);
        } else {
            return null;
        }
    };

    private Category mapResultSetToCategory(ResultSet resultSet) throws SQLException {
        return Category.builder()
                .id(resultSet.getString("id"))
                .name(resultSet.getString("name"))
                .type(Category.CategoryType.valueOf(resultSet.getString("type")))
                .build();
    }
}
