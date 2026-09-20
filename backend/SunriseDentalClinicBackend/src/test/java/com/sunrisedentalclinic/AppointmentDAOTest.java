package com.sunrisedentalclinic;

import com.sunrisedentalclinic.dao.AppointmentDAO;
import com.sunrisedentalclinic.model.Appointment;
import com.sunrisedentalclinic.model.Dentist;
import com.sunrisedentalclinic.model.Patient;
import com.sunrisedentalclinic.model.Treatment;
import com.sunrisedentalclinic.util.DatabaseConnection;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentDAOTest {

    @Test
    void testAppointmentCrudOperations() throws Exception {

        String appointmentNo = "APT-TEST-001";
        int patientId = 0;

        try {
            // -------------------------------------------------
            // 1. Create a temporary test patient
            // -------------------------------------------------
            String patientSql = """
                    INSERT INTO patients
                    (full_name, address, contact_number, date_of_birth, gender)
                    VALUES (?, ?, ?, ?, ?)
                    RETURNING patient_id
                    """;

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(patientSql)) {

                statement.setString(1, "Appointment Test Patient");
                statement.setString(2, "Colombo");
                statement.setString(3, "0790000000");
                statement.setDate(
                        4,
                        java.sql.Date.valueOf(LocalDate.of(1995, 5, 10))
                );
                statement.setString(5, "Male");

                try (ResultSet resultSet = statement.executeQuery()) {
                    assertTrue(resultSet.next());
                    patientId = resultSet.getInt("patient_id");
                }
            }

            // -------------------------------------------------
            // 2. Get an existing dentist and treatment
            // -------------------------------------------------
            int dentistId;
            String dentistName;
            String dentistSpecialization;
            String dentistContact;
            String dentistAvailability;

            String dentistSql = """
                    SELECT dentist_id, full_name, specialization,
                           contact_number, availability
                    FROM dentists
                    ORDER BY dentist_id
                    LIMIT 1
                    """;

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(dentistSql);
                 ResultSet resultSet = statement.executeQuery()) {

                assertTrue(resultSet.next());

                dentistId = resultSet.getInt("dentist_id");
                dentistName = resultSet.getString("full_name");
                dentistSpecialization =
                        resultSet.getString("specialization");
                dentistContact =
                        resultSet.getString("contact_number");
                dentistAvailability =
                        resultSet.getString("availability");
            }

            int treatmentId;
            String treatmentName;
            String treatmentDescription;
            double treatmentCost;

            String treatmentSql = """
                    SELECT treatment_id, treatment_name,
                           description, treatment_cost
                    FROM treatments
                    ORDER BY treatment_id
                    LIMIT 1
                    """;

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(treatmentSql);
                 ResultSet resultSet = statement.executeQuery()) {

                assertTrue(resultSet.next());

                treatmentId = resultSet.getInt("treatment_id");
                treatmentName = resultSet.getString("treatment_name");
                treatmentDescription =
                        resultSet.getString("description");
                treatmentCost =
                        resultSet.getDouble("treatment_cost");
            }

            Patient patient = new Patient(
                    patientId,
                    "Appointment Test Patient",
                    "Colombo",
                    "0790000000",
                    LocalDate.of(1995, 5, 10),
                    "Male"
            );

            Dentist dentist = new Dentist(
                    dentistId,
                    dentistName,
                    dentistSpecialization,
                    dentistContact,
                    dentistAvailability
            );

            Treatment treatment = new Treatment(
                    treatmentId,
                    treatmentName,
                    treatmentDescription,
                    treatmentCost
            );

            Appointment appointment = new Appointment(
                    appointmentNo,
                    patient,
                    dentist,
                    treatment,
                    LocalDate.now().plusDays(1),
                    LocalTime.of(10, 30),
                    "SCHEDULED",
                    "DAO test appointment"
            );

            AppointmentDAO appointmentDAO = new AppointmentDAO();

            // -------------------------------------------------
            // 3. Test SAVE
            // -------------------------------------------------
            assertTrue(
                    appointmentDAO.save(appointment),
                    "Appointment should be saved successfully"
            );

            // -------------------------------------------------
            // 4. Test FIND BY APPOINTMENT NUMBER
            // -------------------------------------------------
            Appointment found =
                    appointmentDAO.findByAppointmentNo(appointmentNo);

            assertNotNull(found);
            assertEquals(appointmentNo, found.getAppointmentNo());
            assertEquals(
                    "Appointment Test Patient",
                    found.getPatient().getFullName()
            );
            assertEquals(
                    treatmentName,
                    found.getTreatment().getTreatmentName()
            );

            // -------------------------------------------------
            // 5. Test UPDATE
            // -------------------------------------------------
            found.updateStatus("COMPLETED");

            assertTrue(
                    appointmentDAO.update(found),
                    "Appointment should be updated successfully"
            );

            Appointment updated =
                    appointmentDAO.findByAppointmentNo(appointmentNo);

            assertNotNull(updated);
            assertEquals("COMPLETED", updated.getStatus());

            // -------------------------------------------------
            // 6. Test FIND BY DATE
            // -------------------------------------------------
            List<Appointment> appointments =
                    appointmentDAO.findByDate(
                            appointment.getAppointmentDate()
                    );

            assertTrue(
                    appointments.stream()
                            .anyMatch(a ->
                                    appointmentNo.equals(
                                            a.getAppointmentNo()
                                    )
                            )
            );

            // -------------------------------------------------
            // 7. Test DELETE
            // -------------------------------------------------
            assertTrue(
                    appointmentDAO.delete(appointmentNo),
                    "Appointment should be deleted successfully"
            );

            assertNull(
                    appointmentDAO.findByAppointmentNo(appointmentNo),
                    "Deleted appointment should not be found"
            );

        } finally {

            // Delete temporary patient after the test
            if (patientId > 0) {

                String deletePatientSql = """
                        DELETE FROM patients
                        WHERE patient_id = ?
                        """;

                try (Connection connection =
                             DatabaseConnection.getConnection();
                     PreparedStatement statement =
                             connection.prepareStatement(deletePatientSql)) {

                    statement.setInt(1, patientId);
                    statement.executeUpdate();
                }
            }
        }
    }
}