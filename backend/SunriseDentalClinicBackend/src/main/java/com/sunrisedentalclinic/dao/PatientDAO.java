package com.sunrisedentalclinic.dao;

import com.sunrisedentalclinic.model.Patient;
import com.sunrisedentalclinic.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public Patient findById(int patientId) {

        String sql = """
                SELECT patient_id, full_name, address, contact_number,
                       date_of_birth, gender
                FROM patients
                WHERE patient_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, patientId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    Date dob = resultSet.getDate("date_of_birth");

                    return new Patient(
                            resultSet.getInt("patient_id"),
                            resultSet.getString("full_name"),
                            resultSet.getString("address"),
                            resultSet.getString("contact_number"),
                            dob != null ? dob.toLocalDate() : null,
                            resultSet.getString("gender")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding patient by ID", e);
        }

        return null;
    }

    public boolean save(Patient patient) {

        String sql = """
                INSERT INTO patients
                (full_name, address, contact_number, date_of_birth, gender)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, patient.getFullName());
            statement.setString(2, patient.getAddress());
            statement.setString(3, patient.getContactNumber());

            if (patient.getDateOfBirth() != null) {
                statement.setDate(
                        4,
                        Date.valueOf(patient.getDateOfBirth())
                );
            } else {
                statement.setNull(4, Types.DATE);
            }

            statement.setString(5, patient.getGender());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving patient", e);
        }
    }

    public boolean update(Patient patient) {

        String sql = """
                UPDATE patients
                SET full_name = ?,
                    address = ?,
                    contact_number = ?,
                    date_of_birth = ?,
                    gender = ?
                WHERE patient_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, patient.getFullName());
            statement.setString(2, patient.getAddress());
            statement.setString(3, patient.getContactNumber());

            if (patient.getDateOfBirth() != null) {
                statement.setDate(
                        4,
                        Date.valueOf(patient.getDateOfBirth())
                );
            } else {
                statement.setNull(4, Types.DATE);
            }

            statement.setString(5, patient.getGender());
            statement.setInt(6, patient.getPatientId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating patient", e);
        }
    }

    public List<Patient> findAll() {

        String sql = """
                SELECT patient_id, full_name, address, contact_number,
                       date_of_birth, gender
                FROM patients
                ORDER BY patient_id
                """;

        List<Patient> patients = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Date dob = resultSet.getDate("date_of_birth");

                Patient patient = new Patient(
                        resultSet.getInt("patient_id"),
                        resultSet.getString("full_name"),
                        resultSet.getString("address"),
                        resultSet.getString("contact_number"),
                        dob != null ? dob.toLocalDate() : null,
                        resultSet.getString("gender")
                );

                patients.add(patient);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving patients", e);
        }

        return patients;
    }
}