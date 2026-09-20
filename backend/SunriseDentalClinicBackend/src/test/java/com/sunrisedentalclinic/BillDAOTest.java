package com.sunrisedentalclinic;

import com.sunrisedentalclinic.dao.BillDAO;
import com.sunrisedentalclinic.model.Bill;
import com.sunrisedentalclinic.util.DatabaseConnection;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BillDAOTest {

    @Test
    void testBillCrudOperations() throws Exception {

        String appointmentNo = "BILL-TEST-001";
        int appointmentPatientId = 0;

        try {
            // 1. Create temporary patient
            String patientSql = """
                    INSERT INTO patients
                    (full_name, address, contact_number, date_of_birth, gender)
                    VALUES (?, ?, ?, ?, ?)
                    RETURNING patient_id
                    """;

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(patientSql)) {

                statement.setString(1, "Bill Test Patient");
                statement.setString(2, "Colombo");
                statement.setString(3, "0780000000");
                statement.setDate(
                        4,
                        java.sql.Date.valueOf(LocalDate.of(1998, 3, 20))
                );
                statement.setString(5, "Female");

                try (ResultSet resultSet = statement.executeQuery()) {
                    assertTrue(resultSet.next());
                    appointmentPatientId =
                            resultSet.getInt("patient_id");
                }
            }

            // 2. Get an existing dentist
            int dentistId;

            String dentistSql = """
                    SELECT dentist_id
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
            }

            // 3. Get an existing treatment
            int treatmentId;
            double treatmentCost;

            String treatmentSql = """
                    SELECT treatment_id, treatment_cost
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
                treatmentCost = resultSet.getDouble("treatment_cost");
            }

            // 4. Create temporary appointment
            String appointmentSql = """
                    INSERT INTO appointments
                    (
                        appointment_no,
                        patient_id,
                        dentist_id,
                        treatment_id,
                        appointment_date,
                        appointment_time,
                        status,
                        notes
                    )
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """;

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(appointmentSql)) {

                statement.setString(1, appointmentNo);
                statement.setInt(2, appointmentPatientId);
                statement.setInt(3, dentistId);
                statement.setInt(4, treatmentId);
                statement.setDate(
                        5,
                        java.sql.Date.valueOf(LocalDate.now().plusDays(1))
                );
                statement.setTime(
                        6,
                        java.sql.Time.valueOf("11:00:00")
                );
                statement.setString(7, "SCHEDULED");
                statement.setString(8, "Bill DAO test");

                assertEquals(1, statement.executeUpdate());
            }

            // 5. Create Bill
            double consultationFee = 2000.00;
            double totalAmount = treatmentCost + consultationFee;

            Bill bill = new Bill(
                    0,
                    appointmentNo,
                    LocalDate.now(),
                    treatmentCost,
                    consultationFee,
                    totalAmount,
                    "PENDING"
            );

            BillDAO billDAO = new BillDAO();

            // 6. Test SAVE
            assertTrue(
                    billDAO.save(bill),
                    "Bill should be saved successfully"
            );

            // 7. Test FIND BY APPOINTMENT NUMBER
            Bill savedBill =
                    billDAO.findByAppointmentNo(appointmentNo);

            assertNotNull(savedBill);
            assertEquals(appointmentNo, savedBill.getAppointmentNo());
            assertEquals(
                    totalAmount,
                    savedBill.getTotalAmount(),
                    0.01
            );

            int billId = savedBill.getBillId();

            // 8. Test FIND BY ID
            Bill foundById = billDAO.findById(billId);

            assertNotNull(foundById);
            assertEquals(
                    billId,
                    foundById.getBillId()
            );

            // 9. Test UPDATE
            savedBill.markAsPaid();

            assertTrue(
                    billDAO.update(savedBill),
                    "Bill should be updated successfully"
            );

            Bill updatedBill = billDAO.findById(billId);

            assertNotNull(updatedBill);
            assertEquals(
                    "PAID",
                    updatedBill.getPaymentStatus()
            );

        } finally {

            // Remove temporary bill
            String deleteBillSql = """
                    DELETE FROM bills
                    WHERE appointment_no = ?
                    """;

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(deleteBillSql)) {

                statement.setString(1, appointmentNo);
                statement.executeUpdate();
            }

            // Remove temporary appointment
            String deleteAppointmentSql = """
                    DELETE FROM appointments
                    WHERE appointment_no = ?
                    """;

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(deleteAppointmentSql)) {

                statement.setString(1, appointmentNo);
                statement.executeUpdate();
            }

            // Remove temporary patient
            if (appointmentPatientId > 0) {

                String deletePatientSql = """
                        DELETE FROM patients
                        WHERE patient_id = ?
                        """;

                try (Connection connection =
                             DatabaseConnection.getConnection();
                     PreparedStatement statement =
                             connection.prepareStatement(deletePatientSql)) {

                    statement.setInt(1, appointmentPatientId);
                    statement.executeUpdate();
                }
            }
        }
    }
}