package com.td2.wallet.repository;

import com.td2.wallet.model.Category;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    public BigDecimal calculateCategorySumBetweenDates(String accountId, LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal restaurantSum = BigDecimal.ZERO;
        BigDecimal salaireSum = BigDecimal.ZERO;

        // Requête pour récupérer les montants et noms des catégories pour un compte et une dates donnés
        try {
            String sql = "SELECT t.amount, c.name " +
                    "FROM transaction t " +
                    "LEFT JOIN category c ON t.category_id = c.id " +
                    "WHERE t.account_id = ? " +
                    "AND t.transaction_date >= ? " +
                    "AND t.transaction_date <= ?";

            // Exécution de la requête et stockage des résultats dans une liste de maps
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                    sql, accountId, startDate, endDate);

            // Parcourt chaque ligne des resultats obtenue
            for (Map<String, Object> row : rows) {
                // Récupère le nom de la catégorie et le montant de la transaction depuis chaque ligne
                String categoryName = (String) row.get("name");
                BigDecimal amount = (BigDecimal) row.get("amount");

                // Incrémente la somme pour la catégorie 'Restaurant' ou 'Salaire'
                if ("Restaurant".equals(categoryName)) {
                    restaurantSum = restaurantSum.add(amount);
                } else if ("Salaire".equals(categoryName)) {
                    salaireSum = salaireSum.add(amount);
                }
            }
        } catch (DataAccessException e) {
            // Gére les exceptions
            e.printStackTrace();
        }
        // Retourne la somme totale des montants pour 'Restaurant' et 'Salaire'
        return restaurantSum.add(salaireSum);

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
