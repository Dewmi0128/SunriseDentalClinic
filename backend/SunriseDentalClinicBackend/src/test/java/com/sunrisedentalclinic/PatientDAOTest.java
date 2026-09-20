package com.sunrisedentalclinic;

import com.sunrisedentalclinic.dao.PatientDAO;
import com.sunrisedentalclinic.model.Patient;
import com.sunrisedentalclinic.util.DatabaseConnection;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatientDAOTest {

    @Test
    void testSaveAndFindAll() throws Exception {

        String testContact = "0700000000";

        Patient patient = new Patient(
                0,
                "Test Patient",
                "Colombo",
                testContact,
                LocalDate.of(2000, 1, 15),
                "Female"
        );

        PatientDAO patientDAO = new PatientDAO();

        try {
            boolean saved = patientDAO.save(patient);

            assertTrue(saved, "Patient should be saved successfully");

            List<Patient> patients = patientDAO.findAll();

            assertTrue(
                    patients.stream()
                            .anyMatch(p -> testContact.equals(p.getContactNumber())),
                    "Saved patient should be found in the database"
            );

        } finally {

            String sql = "DELETE FROM patients WHERE contact_number = ?";

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, testContact);
                statement.executeUpdate();
            }
        }
    }
}