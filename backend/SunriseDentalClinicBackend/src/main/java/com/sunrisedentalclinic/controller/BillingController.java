package com.sunrisedentalclinic.controller;

import com.sunrisedentalclinic.model.Bill;
import com.sunrisedentalclinic.service.BillingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bills")
public class BillingController {

    private final BillingService billingService;

    public BillingController() {
        this.billingService = new BillingService();
    }

    /**
     * Generate a bill for an appointment.
     *
     * POST /api/bills/appointment/{appointmentNo}
     */
    @PostMapping("/appointment/{appointmentNo}")
    public ResponseEntity<?> generateBill(
            @PathVariable String appointmentNo) {

        Bill bill = billingService.generateBill(appointmentNo);

        if (bill == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Unable to generate bill for appointment: "
                            + appointmentNo);
        }

        return ResponseEntity.ok(bill);
    }

    /**
     * Retrieve a bill using the appointment number.
     *
     * GET /api/bills/appointment/{appointmentNo}
     */
    @GetMapping("/appointment/{appointmentNo}")
    public ResponseEntity<?> getBillByAppointment(
            @PathVariable String appointmentNo) {

        Bill bill = billingService.getBillByAppointment(appointmentNo);

        if (bill == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Bill not found for appointment: "
                            + appointmentNo);
        }

        return ResponseEntity.ok(bill);
    }

    /**
     * Mark a bill as paid.
     *
     * PUT /api/bills/{billId}/pay
     */
    @PutMapping("/{billId}/pay")
    public ResponseEntity<?> markBillAsPaid(
            @PathVariable int billId) {

        boolean updated = billingService.markBillAsPaid(billId);

        if (!updated) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Unable to mark bill as paid: "
                            + billId);
        }

        return ResponseEntity.ok(
                "Bill " + billId + " marked as PAID successfully"
        );
    }

    /**
     * Print/generate a receipt for a bill.
     *
     * GET /api/bills/{billId}/receipt
     */
    @GetMapping("/{billId}/receipt")
    public ResponseEntity<?> printReceipt(
            @PathVariable int billId) {

        String receipt = billingService.printReceipt(billId);

        if (receipt == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Bill not found: " + billId);
        }

        return ResponseEntity.ok(receipt);
    }
}