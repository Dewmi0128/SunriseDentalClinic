package com.sunrisedentalclinic.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Dentist {

    private int dentistId;
    private String fullName;
    private String specialization;
    private String contactNumber;
    private String availability;

    public Dentist(int dentistId, String fullName, String specialization,
                   String contactNumber, String availability) {
        this.dentistId = dentistId;
        this.fullName = fullName;
        this.specialization = specialization;
        this.contactNumber = contactNumber;
        this.availability = availability;
    }

    public int getDentistId() {
        return dentistId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getAvailability() {
        return availability;
    }

    public String getDetails() {
        return fullName + " - " + specialization;
    }

    public boolean isAvailable(LocalDate date, LocalTime time) {
        return availability != null && !availability.isBlank();
    }
}