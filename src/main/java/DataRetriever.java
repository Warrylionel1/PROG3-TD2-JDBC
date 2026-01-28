import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataRetriever {
    Dish findDishById(Integer id) {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                            select dish.id as dish_id, dish.name as dish_name, dish_type, dish.price as dish_price
                                from dish
                                where dish.id = ?;
                            """);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<DishIngredient> ingredients = new ArrayList<>();
            if (resultSet.next()) {
                Dish dish = new Dish();
                dish.setId(resultSet.getInt("dish_id"));
                dish.setName(resultSet.getString("dish_name"));
                dish.setDishType(DishTypeEnum.valueOf(resultSet.getString("dish_type")));

                double dishPrice = resultSet.getDouble("dish_price");
                if (!resultSet.wasNull()) {
                    dish.setPrice(dishPrice);
                }
                List<DishIngredient> dishIngredients = findDishIngredientByDishId(id);
                dish.setIngredients(dishIngredients);

                return dish;
            }
            dbConnection.closeConnection(connection);
            throw new RuntimeException("Dish not found " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Dish saveDish(Dish toSave) {
        String upsertDishSql = """
                    INSERT INTO dish (id, name, dish_type, selling_price)
                            VALUES (?, ?, ?::dish_type, ?)
                            ON CONFLICT (id) DO UPDATE
                            SET name = EXCLUDED.name,
                                dish_type = EXCLUDED.dish_type,
                                selling_price = EXCLUDED.selling_price
                            RETURNING id
                """;

        try (Connection conn = new DBConnection().getConnection()) {
            conn.setAutoCommit(false);
            Integer dishId;
            try (PreparedStatement ps = conn.prepareStatement(upsertDishSql)) {
                if (toSave.getId() != null) {
                    ps.setInt(1, toSave.getId());
                } else {
                    ps.setInt(1, getNextSerialValue(conn, "dish", "id"));
                }
                ps.setString(2, toSave.getName());
                ps.setString(3, toSave.getDishType().name());
                if (toSave.getPrice() != null) {
                    ps.setDouble(4, toSave.getPrice());
                } else {
                    ps.setNull(4, Types.DOUBLE);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    dishId = rs.getInt(1);
                }
            }

            List<DishIngredient> newIngredients = toSave.getIngredients();
            detachIngredients(conn, dishId, newIngredients);
            attachIngredients(conn, dishId, newIngredients);

            conn.commit();
            return findDishById(dishId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ingredient> createIngredients(List<Ingredient> newIngredients) {
        if (newIngredients == null || newIngredients.isEmpty()) {
            return List.of();
        }
        List<Ingredient> savedIngredients = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        Connection conn = dbConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            String insertSql = """
                        INSERT INTO ingredient (id, name, category, price)
                        VALUES (?, ?, ?::ingredient_category, ?)
                        RETURNING id
                    """;
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                for (Ingredient ingredient : newIngredients) {
                    if (ingredient.getId() != null) {
                        ps.setInt(1, ingredient.getId());
                    } else {
                        ps.setInt(1, getNextSerialValue(conn, "ingredient", "id"));
                    }
                    ps.setString(2, ingredient.getName());
                    ps.setString(3, ingredient.getCategory().name());
                    ps.setDouble(4, ingredient.getPrice());

                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        int generatedId = rs.getInt(1);
                        ingredient.setId(generatedId);
                        savedIngredients.add(ingredient);
                    }
                }
                conn.commit();
                return savedIngredients;
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnection.closeConnection(conn);
        }
    }


    private void detachIngredients(Connection conn, Integer dishId, List<DishIngredient> ingredients)
            throws SQLException {
        if (ingredients == null || ingredients.isEmpty()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM dish_ingredient WHERE (id_dish = ?) ")) {
                ps.setInt(1, dishId);
                ps.executeUpdate();
            }
            return;
        }

        String baseSql = """
                    DELETE FROM dish_ingredient
                    WHERE id_dish = ? AND id_ingredient  NOT IN (%s)
                """;

        String inClause = ingredients.stream()
                .map(i -> "?")
                .collect(Collectors.joining(","));

        String sql = String.format(baseSql, inClause);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dishId);
            int index = 2;
            for (DishIngredient dishIngredient : ingredients) {
                ps.setInt(index++, dishIngredient.getIngredient().getId());
            }
            ps.executeUpdate();
        }
    }

    private void attachIngredients(Connection conn, Integer dishId, List<DishIngredient> ingredients)
            throws SQLException {

        if (ingredients == null || ingredients.isEmpty()) {
            return;
        }

        String attachSql = """
                    INSERT INTO dish_ingredient (id_dish, id_ingredient, quantity_required, unit)
                                    VALUES (?, ?, ?, ?)
                                    ON CONFLICT (id_dish, id_ingredient) DO UPDATE
                                    SET quantity_required = EXCLUDED.quantity_required,
                                        unit = EXCLUDED.unit
                """;

        try (PreparedStatement ps = conn.prepareStatement(attachSql)) {
            for (DishIngredient dishIngredient : ingredients) {
                ps.setInt(1, dishId);
                ps.setInt(2, dishIngredient.getIngredient().getId());
                ps.setDouble(3, dishIngredient.getQuantity());

                if (dishIngredient.getUnit() != null) {
                    ps.setString(4, dishIngredient.getUnit().name());
                } else {
                    ps.setNull(4, Types.VARCHAR);
                }

                ps.addBatch();
            }
            ps.executeBatch();
        }
    }


    public List<DishIngredient> findDishIngredientByDishId(Integer idDish) {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        List<DishIngredient> dishIngredients = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                            SELECT
                                                di.id as dish_ingredient_id,
                                                di.quantity_required,
                                                di.unit,
                                                i.id as ingredient_id,
                                                i.name as ingredient_name,
                                                i.price as ingredient_price,
                                                i.category as ingredient_category,
                                                d.id as dish_id,
                                                d.name as dish_name,
                                                d.dish_type,
                                                d.selling_price
                                            FROM dish_ingredient di
                                            INNER JOIN ingredient i ON di.id_ingredient = i.id
                                            INNER JOIN dish d ON di.id_dish = d.id
                                            WHERE di.id_dish = ?
                                            ORDER BY i.id;
                            """);
            preparedStatement.setInt(1, idDish);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(resultSet.getInt("ingredient_id"));
                ingredient.setName(resultSet.getString("ingredient_name"));
                ingredient.setPrice(resultSet.getDouble("ingredient_price"));
                ingredient.setCategory(CategoryEnum.valueOf(resultSet.getString("ingredient_category")));

                Dish dish = new Dish();
                dish.setId(resultSet.getInt("dish_id"));
                dish.setName(resultSet.getString("dish_name"));
                dish.setDishType(DishTypeEnum.valueOf(resultSet.getString("dish_type")));

                double dishPrice = resultSet.getDouble("selling_price");
                if (!resultSet.wasNull()) {
                    dish.setPrice(dishPrice);
                }

                DishIngredient dishIngredient = new DishIngredient();
                dishIngredient.setId(resultSet.getInt("dish_ingredient_id"));
                dishIngredient.setDish(dish);
                dishIngredient.setIngredient(ingredient);
                dishIngredient.setQuantity(resultSet.getDouble("quantity_required"));

                String unitString = resultSet.getString("unit");
                if (unitString != null && !unitString.trim().isEmpty()) {
                    try {
                        dishIngredient.setUnit(UnitEnum.valueOf(unitString.trim().toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        dishIngredient.setUnit(null);
                    }
                }

                dishIngredients.add(dishIngredient);
            }
            dbConnection.closeConnection(connection);
            return dishIngredients;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Ingredient saveIngredient(Ingredient toSave) {
        try (Connection conn = new DBConnection().getConnection()) {
            conn.setAutoCommit(false);

            String upsertIngredientSql = """
                INSERT INTO ingredient (id, name, category, price)
                VALUES (?, ?, ?::ingredient_category, ?)
                ON CONFLICT (id) DO UPDATE
                SET name = EXCLUDED.name,
                    category = EXCLUDED.category,
                    price = EXCLUDED.price
                RETURNING id
            """;

            Integer ingredientId;
            try (PreparedStatement ps = conn.prepareStatement(upsertIngredientSql)) {
                if (toSave.getId() != null && toSave.getId() > 0) {
                    ps.setInt(1, toSave.getId());
                } else {
                    ps.setInt(1, getNextSerialValue(conn, "ingredient", "id"));
                }

                ps.setString(2, toSave.getName());
                ps.setString(3, toSave.getCategory().name());
                ps.setDouble(4, toSave.getPrice());

                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    ingredientId = rs.getInt(1);
                    toSave.setId(ingredientId);
                }
            }

            if (toSave.getStockMovementList() != null && !toSave.getStockMovementList().isEmpty()) {
                String insertMovementSql = """
                    INSERT INTO stock_movement (id, id_ingredient, quantity, type, unit, creation_datetime)
                    VALUES (?, ?, ?, ?, ?::unit_enum, ?)
                    ON CONFLICT (id) DO NOTHING
                """;

                try (PreparedStatement ps = conn.prepareStatement(insertMovementSql)) {
                    for (StockMovement movement : toSave.getStockMovementList()) {
                        ps.setInt(1, movement.getId());
                        ps.setInt(2, ingredientId);

                        StockValue value = movement.getValue();
                        ps.setDouble(3, value.getQuantity());
                        ps.setString(4, movement.getType().name());
                        ps.setString(5, value.getUnit().name());
                        ps.setTimestamp(6, Timestamp.from(movement.getCreationDateTime()));

                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

            conn.commit();
            return toSave;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving ingredient", e);
        }
    }


    public Double getDishCost(Integer dishId) {
        try (Connection conn = new DBConnection().getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    """
                        SELECT SUM(i.price * di.quantity_required) as total_cost
                        FROM dish_ingredient di
                        JOIN ingredient i ON di.id_ingredient = i.id
                        WHERE di.id_dish = ?
                        GROUP BY di.id_dish
                    """);
            ps.setInt(1, dishId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total_cost");
            }
            return 0.0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getSerialSequenceName(Connection conn, String tableName, String columnName)
            throws SQLException {

        String sql = "SELECT pg_get_serial_sequence(?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tableName);
            ps.setString(2, columnName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return null;
    }

    private int getNextSerialValue(Connection conn, String tableName, String columnName)
            throws SQLException {

        String sequenceName = getSerialSequenceName(conn, tableName, columnName);
        if (sequenceName == null) {
            throw new IllegalArgumentException(
                    "Any sequence found for " + tableName + "." + columnName
            );
        }
        updateSequenceNextValue(conn, tableName, columnName, sequenceName);

        String nextValSql = "SELECT nextval(?)";

        try (PreparedStatement ps = conn.prepareStatement(nextValSql)) {
            ps.setString(1, sequenceName);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private void updateSequenceNextValue(Connection conn, String tableName, String columnName, String sequenceName) throws SQLException {
        String setValSql = String.format(
                "SELECT setval('%s', (SELECT COALESCE(MAX(%s), 0) FROM %s))",
                sequenceName, columnName, tableName
        );

        try (PreparedStatement ps = conn.prepareStatement(setValSql)) {
            ps.executeQuery();
        }
    }
}
