package com.sunrisedentalclinic.service;

import com.sunrisedentalclinic.dao.AppointmentDAO;
import com.sunrisedentalclinic.dao.BillDAO;
import com.sunrisedentalclinic.model.Appointment;
import com.sunrisedentalclinic.model.Bill;

public class BillingService {

    private static final double CONSULTATION_FEE = 2000.00;

    private final AppointmentDAO appointmentDAO;
    private final BillDAO billDAO;

    public BillingService() {
        this.appointmentDAO = new AppointmentDAO();
        this.billDAO = new BillDAO();
    }

    /**
     * Calculates a bill for an existing appointment.
     */
    public Bill calculateBill(String appointmentNo) {

        if (appointmentNo == null || appointmentNo.isBlank()) {
            return null;
        }

        Appointment appointment =
                appointmentDAO.findByAppointmentNo(appointmentNo);

        if (appointment == null) {
            return null;
        }

        double treatmentCost = appointment.getTreatment().getCost();

        return new Bill(
                0,
                appointmentNo,
                java.time.LocalDate.now(),
                treatmentCost,
                CONSULTATION_FEE,
                treatmentCost + CONSULTATION_FEE,
                "PENDING"
        );
    }

    /**
     * Generates and saves a bill for an appointment.
     */
    public Bill generateBill(String appointmentNo) {

        if (appointmentNo == null || appointmentNo.isBlank()) {
            return null;
        }

        // Prevent duplicate bills for the same appointment.
        Bill existingBill =
                billDAO.findByAppointmentNo(appointmentNo);

        if (existingBill != null) {
            return existingBill;
        }

        Bill bill = calculateBill(appointmentNo);

        if (bill == null) {
            return null;
        }

        if (!billDAO.save(bill)) {
            return null;
        }

        return billDAO.findByAppointmentNo(appointmentNo);
    }

    /**
     * Retrieves a bill using its appointment number.
     */
    public Bill getBillByAppointment(String appointmentNo) {

        if (appointmentNo == null || appointmentNo.isBlank()) {
            return null;
        }

        return billDAO.findByAppointmentNo(appointmentNo);
    }

    /**
     * Marks an existing bill as paid.
     */
    public boolean markBillAsPaid(int billId) {

        if (billId <= 0) {
            return false;
        }

        Bill bill = billDAO.findById(billId);

        if (bill == null) {
            return false;
        }

        bill.markAsPaid();

        return billDAO.update(bill);
    }

    /**
     * Generates the receipt text for an existing bill.
     */
    public String printReceipt(int billId) {

        if (billId <= 0) {
            return null;
        }

        Bill bill = billDAO.findById(billId);

        if (bill == null) {
            return null;
        }

        return bill.generateReceipt();
    }
}