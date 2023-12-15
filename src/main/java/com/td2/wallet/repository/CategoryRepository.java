package com.td2.wallet.repository;

import com.td2.wallet.model.Category;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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
    public Category findCategoryByType(Category.CategoryType type) {
        String sql = "SELECT * FROM category WHERE type = ?";
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{type.name()},
                    (resultSet, rowNum) -> mapRowToCategory(resultSet)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Category mapRowToCategory(ResultSet resultSet) throws SQLException {
        Category category = new Category();
        category.setId(resultSet.getString("id"));
        category.setName(resultSet.getString("name"));
        category.setType(Category.CategoryType.valueOf(resultSet.getString("type")));
        return category;
    }
    public Category save(Category category) {
        try {
            String query = "INSERT INTO category (id, name, type)\n" +
                    "VALUES (?, ?, CAST(? AS category_type))\n" +
                    "ON CONFLICT (id)\n" +
                    "DO UPDATE SET\n" +
                    "  name = excluded.name,\n" +
                    "  type = excluded.type";

            int rowsAffected = jdbcTemplate.update(
                    query,
                    category.getId(),
                    category.getName(),
                    category.getType().name()  // Assuming category.getType() returns an Enum
            );

            return (rowsAffected > 0) ? category : null;
        } catch (DataAccessException e) {
            // Handle or log the exception as needed
            throw new RuntimeException("Error saving category", e);
        }
    }

}
