package com.sunrisedentalclinic.dao;

import com.sunrisedentalclinic.model.Appointment;
import com.sunrisedentalclinic.model.Dentist;
import com.sunrisedentalclinic.model.Patient;
import com.sunrisedentalclinic.model.Treatment;
import com.sunrisedentalclinic.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public Appointment findByAppointmentNo(String appointmentNo) {

        String sql = """
                SELECT
                    a.appointment_no,
                    a.appointment_date,
                    a.appointment_time,
                    a.status,
                    a.notes,

                    p.patient_id,
                    p.full_name AS patient_name,
                    p.address,
                    p.contact_number,
                    p.date_of_birth,
                    p.gender,

                    d.dentist_id,
                    d.full_name AS dentist_name,
                    d.specialization,
                    d.contact_number AS dentist_contact,
                    d.availability,

                    t.treatment_id,
                    t.treatment_name,
                    t.description,
                    t.treatment_cost

                FROM appointments a

                JOIN patients p
                    ON a.patient_id = p.patient_id

                JOIN dentists d
                    ON a.dentist_id = d.dentist_id

                JOIN treatments t
                    ON a.treatment_id = t.treatment_id

                WHERE a.appointment_no = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, appointmentNo);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Date dateOfBirth = resultSet.getDate("date_of_birth");

                    Patient patient = new Patient(
                            resultSet.getInt("patient_id"),
                            resultSet.getString("patient_name"),
                            resultSet.getString("address"),
                            resultSet.getString("contact_number"),
                            dateOfBirth != null
                                    ? dateOfBirth.toLocalDate()
                                    : null,
                            resultSet.getString("gender")
                    );

                    Dentist dentist = new Dentist(
                            resultSet.getInt("dentist_id"),
                            resultSet.getString("dentist_name"),
                            resultSet.getString("specialization"),
                            resultSet.getString("dentist_contact"),
                            resultSet.getString("availability")
                    );

                    Treatment treatment = new Treatment(
                            resultSet.getInt("treatment_id"),
                            resultSet.getString("treatment_name"),
                            resultSet.getString("description"),
                            resultSet.getDouble("treatment_cost")
                    );

                    return new Appointment(
                            resultSet.getString("appointment_no"),
                            patient,
                            dentist,
                            treatment,
                            resultSet.getDate("appointment_date").toLocalDate(),
                            resultSet.getTime("appointment_time").toLocalTime(),
                            resultSet.getString("status"),
                            resultSet.getString("notes")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding appointment by appointment number", e
            );
        }

        return null;
    }

    public boolean save(Appointment appointment) {

        String sql = """
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
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, appointment.getAppointmentNo());
            statement.setInt(2, appointment.getPatient().getPatientId());
            statement.setInt(3, appointment.getDentist().getDentistId());
            statement.setInt(4, appointment.getTreatment().getTreatmentId());
            statement.setDate(
                    5,
                    Date.valueOf(appointment.getAppointmentDate())
            );
            statement.setTime(
                    6,
                    Time.valueOf(appointment.getAppointmentTime())
            );
            statement.setString(7, appointment.getStatus());
            statement.setString(8, appointment.getNotes());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving appointment", e);
        }
    }

    public boolean update(Appointment appointment) {

        String sql = """
                UPDATE appointments
                SET patient_id = ?,
                    dentist_id = ?,
                    treatment_id = ?,
                    appointment_date = ?,
                    appointment_time = ?,
                    status = ?,
                    notes = ?
                WHERE appointment_no = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, appointment.getPatient().getPatientId());
            statement.setInt(2, appointment.getDentist().getDentistId());
            statement.setInt(3, appointment.getTreatment().getTreatmentId());
            statement.setDate(
                    4,
                    Date.valueOf(appointment.getAppointmentDate())
            );
            statement.setTime(
                    5,
                    Time.valueOf(appointment.getAppointmentTime())
            );
            statement.setString(6, appointment.getStatus());
            statement.setString(7, appointment.getNotes());
            statement.setString(8, appointment.getAppointmentNo());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating appointment", e);
        }
    }

    public boolean delete(String appointmentNo) {

        String sql = """
                DELETE FROM appointments
                WHERE appointment_no = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, appointmentNo);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting appointment", e);
        }
    }

    public List<Appointment> findByDate(LocalDate date) {

        String sql = """
                SELECT appointment_no
                FROM appointments
                WHERE appointment_date = ?
                ORDER BY appointment_time
                """;

        List<Appointment> appointments = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDate(1, Date.valueOf(date));

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Appointment appointment =
                            findByAppointmentNo(
                                    resultSet.getString("appointment_no")
                            );

                    if (appointment != null) {
                        appointments.add(appointment);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error retrieving appointments by date", e
            );
        }

        return appointments;
    }
}