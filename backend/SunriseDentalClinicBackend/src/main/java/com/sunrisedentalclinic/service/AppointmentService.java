package com.sunrisedentalclinic.service;

import com.sunrisedentalclinic.dao.AppointmentDAO;
import com.sunrisedentalclinic.model.Appointment;

import java.time.LocalDate;
import java.util.List;

public class AppointmentService {

    private final AppointmentDAO appointmentDAO;

    public AppointmentService() {
        this.appointmentDAO = new AppointmentDAO();
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

        if (appointmentDAO.findByAppointmentNo(appointmentNo) == null) {
            return false;
        }

        return appointmentDAO.delete(appointmentNo);
    }
}