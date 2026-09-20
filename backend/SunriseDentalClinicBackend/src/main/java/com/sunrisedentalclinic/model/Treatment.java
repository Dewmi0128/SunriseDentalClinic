package com.sunrisedentalclinic.model;

public class Treatment {

    private int treatmentId;
    private String treatmentName;
    private String description;
    private double treatmentCost;

    public Treatment(int treatmentId, String treatmentName,
                     String description, double treatmentCost) {
        this.treatmentId = treatmentId;
        this.treatmentName = treatmentName;
        this.description = description;
        this.treatmentCost = treatmentCost;
    }

    public int getTreatmentId() {
        return treatmentId;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public String getDescription() {
        return description;
    }

    public double getCost() {
        return treatmentCost;
    }

    public String getTreatmentDetails() {
        return treatmentName + " - " + description;
    }
}