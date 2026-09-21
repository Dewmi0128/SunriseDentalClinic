package com.sunrisedentalclinic;

import com.sunrisedentalclinic.model.Bill;
import com.sunrisedentalclinic.service.BillingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BillingServiceTest {

    private final BillingService billingService =
            new BillingService();

    @Test
    void testCalculateBillWithEmptyAppointmentNumber() {

        Bill bill = billingService.calculateBill("");

        assertNull(bill);
    }

    @Test
    void testCalculateBillWithUnknownAppointment() {

        Bill bill = billingService.calculateBill(
                "UNKNOWN-APPOINTMENT-999");

        assertNull(bill);
    }

    @Test
    void testGenerateBillWithEmptyAppointmentNumber() {

        Bill bill = billingService.generateBill("");

        assertNull(bill);
    }

    @Test
    void testGenerateBillWithUnknownAppointment() {

        Bill bill = billingService.generateBill(
                "UNKNOWN-APPOINTMENT-999");

        assertNull(bill);
    }

    @Test
    void testGetBillWithEmptyAppointmentNumber() {

        Bill bill = billingService.getBillByAppointment("");

        assertNull(bill);
    }

    @Test
    void testMarkBillAsPaidWithInvalidId() {

        assertFalse(
                billingService.markBillAsPaid(0)
        );
    }

    @Test
    void testMarkBillAsPaidWithUnknownId() {

        assertFalse(
                billingService.markBillAsPaid(999999)
        );
    }

    @Test
    void testPrintReceiptWithInvalidId() {

        assertNull(
                billingService.printReceipt(0)
        );
    }

    @Test
    void testPrintReceiptWithUnknownId() {

        assertNull(
                billingService.printReceipt(999999)
        );
    }
}