package com.sunrisedentalclinic.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {

    private String appointmentNo;
    private Patient patient;
    private Dentist dentist;
    private Treatment treatment;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;
    private String notes;

    public Appointment(String appointmentNo,
                       Patient patient,
                       Dentist dentist,
                       Treatment treatment,
                       LocalDate appointmentDate,
                       LocalTime appointmentTime,
                       String status,
                       String notes) {

        this.appointmentNo = appointmentNo;
        this.patient = patient;
        this.dentist = dentist;
        this.treatment = treatment;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.notes = notes;
    }

    public String getAppointmentNo() {
        return appointmentNo;
    }

    public Patient getPatient() {
        return patient;
    }

    public Dentist getDentist() {
        return dentist;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public String getAppointmentDetails() {
        return appointmentNo + " - " +
                patient.getFullName() + " - " +
                appointmentDate + " " +
                appointmentTime;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public void updateStatus(String status) {
        this.status = status;
    }
}