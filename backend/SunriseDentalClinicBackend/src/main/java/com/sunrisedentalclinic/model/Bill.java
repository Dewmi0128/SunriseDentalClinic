package com.sunrisedentalclinic.model;

import java.time.LocalDate;

public class Bill {

    private int billId;
    private String appointmentNo;
    private LocalDate billDate;
    private double treatmentCost;
    private double consultationFee;
    private double totalAmount;
    private String paymentStatus;

    public Bill(int billId,
                String appointmentNo,
                LocalDate billDate,
                double treatmentCost,
                double consultationFee,
                double totalAmount,
                String paymentStatus) {

        this.billId = billId;
        this.appointmentNo = appointmentNo;
        this.billDate = billDate;
        this.treatmentCost = treatmentCost;
        this.consultationFee = consultationFee;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
    }

    public int getBillId() {
        return billId;
    }

    public String getAppointmentNo() {
        return appointmentNo;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public double getTreatmentCost() {
        return treatmentCost;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public double calculateTotal() {
        totalAmount = treatmentCost + consultationFee;
        return totalAmount;
    }

    public String generateReceipt() {
        return "Bill No: " + billId
                + "\nAppointment No: " + appointmentNo
                + "\nTreatment Cost: " + treatmentCost
                + "\nConsultation Fee: " + consultationFee
                + "\nTotal Amount: " + totalAmount
                + "\nPayment Status: " + paymentStatus;
    }

    public void markAsPaid() {
        this.paymentStatus = "PAID";
    }
}