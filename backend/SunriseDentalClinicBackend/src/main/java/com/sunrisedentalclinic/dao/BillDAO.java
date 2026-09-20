package com.sunrisedentalclinic.dao;

import com.sunrisedentalclinic.model.Bill;
import com.sunrisedentalclinic.util.DatabaseConnection;

import java.sql.*;

public class BillDAO {

    public Bill findById(int billId) {

        String sql = """
                SELECT bill_id, appointment_no, bill_date,
                       treatment_cost, consultation_fee,
                       total_amount, payment_status
                FROM bills
                WHERE bill_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, billId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return new Bill(
                            resultSet.getInt("bill_id"),
                            resultSet.getString("appointment_no"),
                            resultSet.getDate("bill_date").toLocalDate(),
                            resultSet.getDouble("treatment_cost"),
                            resultSet.getDouble("consultation_fee"),
                            resultSet.getDouble("total_amount"),
                            resultSet.getString("payment_status")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding bill by ID", e);
        }

        return null;
    }

    public boolean save(Bill bill) {

        String sql = """
                INSERT INTO bills
                (
                    appointment_no,
                    bill_date,
                    treatment_cost,
                    consultation_fee,
                    total_amount,
                    payment_status
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, bill.getAppointmentNo());
            statement.setDate(
                    2,
                    Date.valueOf(bill.getBillDate())
            );
            statement.setDouble(3, bill.getTreatmentCost());
            statement.setDouble(4, bill.getConsultationFee());
            statement.setDouble(5, bill.getTotalAmount());
            statement.setString(6, bill.getPaymentStatus());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving bill", e);
        }
    }

    public boolean update(Bill bill) {

        String sql = """
                UPDATE bills
                SET treatment_cost = ?,
                    consultation_fee = ?,
                    total_amount = ?,
                    payment_status = ?
                WHERE bill_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDouble(1, bill.getTreatmentCost());
            statement.setDouble(2, bill.getConsultationFee());
            statement.setDouble(3, bill.getTotalAmount());
            statement.setString(4, bill.getPaymentStatus());
            statement.setInt(5, bill.getBillId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating bill", e);
        }
    }

    public Bill findByAppointmentNo(String appointmentNo) {

        String sql = """
                SELECT bill_id, appointment_no, bill_date,
                       treatment_cost, consultation_fee,
                       total_amount, payment_status
                FROM bills
                WHERE appointment_no = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, appointmentNo);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return new Bill(
                            resultSet.getInt("bill_id"),
                            resultSet.getString("appointment_no"),
                            resultSet.getDate("bill_date").toLocalDate(),
                            resultSet.getDouble("treatment_cost"),
                            resultSet.getDouble("consultation_fee"),
                            resultSet.getDouble("total_amount"),
                            resultSet.getString("payment_status")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding bill by appointment number", e
            );
        }

        return null;
    }
}