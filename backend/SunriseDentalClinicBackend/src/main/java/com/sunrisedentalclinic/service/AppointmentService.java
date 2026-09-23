package com.sunrisedentalclinic.service;

import com.sunrisedentalclinic.dao.AppointmentDAO;
import com.sunrisedentalclinic.dao.DentistDAO;
import com.sunrisedentalclinic.dao.PatientDAO;
import com.sunrisedentalclinic.dao.TreatmentDAO;
import com.sunrisedentalclinic.model.Appointment;
import com.sunrisedentalclinic.model.Dentist;
import com.sunrisedentalclinic.model.Patient;
import com.sunrisedentalclinic.model.Treatment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AppointmentService {

    private final AppointmentDAO appointmentDAO;
    private final PatientDAO patientDAO;
    private final DentistDAO dentistDAO;
    private final TreatmentDAO treatmentDAO;

    public AppointmentService() {
        this.appointmentDAO = new AppointmentDAO();
        this.patientDAO = new PatientDAO();
        this.dentistDAO = new DentistDAO();
        this.treatmentDAO = new TreatmentDAO();
    }

    /**
     * Registers a new appointment after validating the required data.
     */
    public boolean registerAppointment(Appointment appointment) {

        if (appointment == null) {
            return false;
        }

        if (appointment.getAppointmentNo() == null ||
                appointment.getAppointmentNo().isBlank()) {
            return false;
        }

        if (appointment.getPatient() == null) {
            return false;
        }

        if (appointment.getDentist() == null) {
            return false;
        }

        if (appointment.getTreatment() == null) {
            return false;
        }

        if (appointment.getAppointmentDate() == null) {
            return false;
        }

        if (appointment.getAppointmentTime() == null) {
            return false;
        }

        // Prevent duplicate appointment numbers.
        if (appointmentDAO.findByAppointmentNo(
                appointment.getAppointmentNo()) != null) {
            return false;
        }

        return appointmentDAO.save(appointment);
    }

    /**
     * Registers an appointment received from the REST API.
     *
     * The API sends patient, dentist and treatment IDs.
     * These IDs are converted into the corresponding domain objects
     * before the appointment is saved.
     */
    public boolean registerAppointment(
            String appointmentNo,
            int patientId,
            int dentistId,
            int treatmentId,
            String appointmentDate,
            String appointmentTime,
            String status,
            String notes) {

        // Validate appointment number.
        if (appointmentNo == null || appointmentNo.isBlank()) {
            return false;
        }

        // Validate date.
        if (appointmentDate == null || appointmentDate.isBlank()) {
            return false;
        }

        // Validate time.
        if (appointmentTime == null || appointmentTime.isBlank()) {
            return false;
        }

        // Prevent duplicate appointment numbers.
        if (appointmentDAO.findByAppointmentNo(appointmentNo) != null) {
            return false;
        }

        // Retrieve related records from the database.
        Patient patient = patientDAO.findById(patientId);
        Dentist dentist = dentistDAO.findById(dentistId);
        Treatment treatment = treatmentDAO.findById(treatmentId);

        // All referenced records must exist.
        if (patient == null || dentist == null || treatment == null) {
            return false;
        }

        try {
            LocalDate date = LocalDate.parse(appointmentDate);
            LocalTime time = LocalTime.parse(appointmentTime);

            String appointmentStatus =
                    (status == null || status.isBlank())
                            ? "SCHEDULED"
                            : status;

            Appointment appointment = new Appointment(
                    appointmentNo,
                    patient,
                    dentist,
                    treatment,
                    date,
                    time,
                    appointmentStatus,
                    notes
            );

            return registerAppointment(appointment);

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Searches for an appointment using its appointment number.
     */
    public Appointment searchAppointment(String appointmentNo) {

        if (appointmentNo == null || appointmentNo.isBlank()) {
            return null;
        }

        return appointmentDAO.findByAppointmentNo(appointmentNo);
    }

    /**
     * Retrieves appointments scheduled for a specific date.
     */
    public List<Appointment> getAppointmentsByDate(LocalDate date) {

        if (date == null) {
            return List.of();
        }

        return appointmentDAO.findByDate(date);
    }

    /**
     * Updates an existing appointment.
     */
    public boolean updateAppointment(Appointment appointment) {

        if (appointment == null ||
                appointment.getAppointmentNo() == null ||
                appointment.getAppointmentNo().isBlank()) {
            return false;
        }

        if (appointmentDAO.findByAppointmentNo(
                appointment.getAppointmentNo()) == null) {
            return false;
        }

        return appointmentDAO.update(appointment);
    }

    /**
     * Deletes an appointment using its appointment number.
     */
    public boolean cancelAppointment(String appointmentNo) {

        if (appointmentNo == null || appointmentNo.isBlank()) {
            return false;
        }

        Appointment appointment =
                appointmentDAO.findByAppointmentNo(appointmentNo);

        if (appointment == null) {
            return false;
        }

        // Do not delete the appointment.
        // Keep the record and change its status to CANCELLED.
        appointment.updateStatus("CANCELLED");

        return appointmentDAO.update(appointment);
    }
}