package com.sunrisedentalclinic.dao;

import com.sunrisedentalclinic.model.Dentist;
import com.sunrisedentalclinic.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DentistDAO {

    public Dentist findById(int dentistId) {

        String sql = """
                SELECT dentist_id, full_name, specialization,
                       contact_number, availability
                FROM dentists
                WHERE dentist_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, dentistId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return new Dentist(
                            resultSet.getInt("dentist_id"),
                            resultSet.getString("full_name"),
                            resultSet.getString("specialization"),
                            resultSet.getString("contact_number"),
                            resultSet.getString("availability")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find dentist", e);
        }

        return null;
    }

    public List<Dentist> findAll() {

        String sql = """
                SELECT dentist_id, full_name, specialization,
                       contact_number, availability
                FROM dentists
                ORDER BY dentist_id
                """;

        List<Dentist> dentists = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Dentist dentist = new Dentist(
                        resultSet.getInt("dentist_id"),
                        resultSet.getString("full_name"),
                        resultSet.getString("specialization"),
                        resultSet.getString("contact_number"),
                        resultSet.getString("availability")
                );

                dentists.add(dentist);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve dentists", e);
        }

        return dentists;
    }
}