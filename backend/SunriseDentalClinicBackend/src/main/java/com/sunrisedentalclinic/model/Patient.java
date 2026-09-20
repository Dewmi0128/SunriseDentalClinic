package com.sunrisedentalclinic.model;

import java.time.LocalDate;

public class Patient {

    private int patientId;
    private String fullName;
    private String address;
    private String contactNumber;
    private LocalDate dateOfBirth;
    private String gender;

    public Patient(int patientId, String fullName, String address,
                   String contactNumber, LocalDate dateOfBirth,
                   String gender) {
        this.patientId = patientId;
        this.fullName = fullName;
        this.address = address;
        this.contactNumber = contactNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getPatientDetails() {
        return fullName + " - " + contactNumber;
    }

    public boolean updatePatientDetails(String address,
                                        String contactNumber) {
        this.address = address;
        this.contactNumber = contactNumber;
        return true;
    }
}