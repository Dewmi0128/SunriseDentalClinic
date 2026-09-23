package com.sunrisedentalclinic.dao;

import com.sunrisedentalclinic.model.Treatment;
import com.sunrisedentalclinic.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TreatmentDAO {

    public Treatment findById(int treatmentId) {

        String sql = """
                SELECT treatment_id, treatment_name,
                       description, treatment_cost
                FROM treatments
                WHERE treatment_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, treatmentId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return new Treatment(
                            resultSet.getInt("treatment_id"),
                            resultSet.getString("treatment_name"),
                            resultSet.getString("description"),
                            resultSet.getDouble("treatment_cost")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find treatment", e);
        }

        return null;
    }

    public List<Treatment> findAll() {

        String sql = """
                SELECT treatment_id, treatment_name,
                       description, treatment_cost
                FROM treatments
                ORDER BY treatment_id
                """;

        List<Treatment> treatments = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Treatment treatment = new Treatment(
                        resultSet.getInt("treatment_id"),
                        resultSet.getString("treatment_name"),
                        resultSet.getString("description"),
                        resultSet.getDouble("treatment_cost")
                );

                treatments.add(treatment);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve treatments", e);
        }

        return treatments;
    }
}