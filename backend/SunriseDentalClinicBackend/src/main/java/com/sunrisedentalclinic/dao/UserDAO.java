package com.sunrisedentalclinic.dao;

import com.sunrisedentalclinic.model.User;
import com.sunrisedentalclinic.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User findByUsername(String username) {

        String sql = """
                SELECT user_id, username, password_hash, role, status
                FROM users
                WHERE username = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return new User(
                            resultSet.getInt("user_id"),
                            resultSet.getString("username"),
                            resultSet.getString("password_hash"),
                            resultSet.getString("role"),
                            resultSet.getString("status")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by username", e);
        }

        return null;
    }
}