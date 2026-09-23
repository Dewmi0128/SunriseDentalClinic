package com.sunrisedentalclinic.controller;

import com.sunrisedentalclinic.model.Appointment;
import com.sunrisedentalclinic.service.AppointmentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController() {
        this.appointmentService = new AppointmentService();
    }

    /**
     * Search for an appointment by appointment number.
     */
    @GetMapping("/{appointmentNo}")
    public Appointment getAppointment(
            @PathVariable String appointmentNo) {

        return appointmentService.searchAppointment(appointmentNo);
    }

    /**
     * Register a new appointment.
     */
    @PostMapping
    public String createAppointment(
            @RequestBody AppointmentRequest request) {

        boolean created = appointmentService.registerAppointment(
                request.getAppointmentNo(),
                request.getPatientId(),
                request.getDentistId(),
                request.getTreatmentId(),
                request.getAppointmentDate(),
                request.getAppointmentTime(),
                request.getStatus(),
                request.getNotes()
        );

        if (created) {
            return "Appointment registered successfully";
        }

        return "Unable to register appointment";
    }

    @PutMapping("/{appointmentNo}/cancel")
    public ResponseEntity<String> cancelAppointment(
            @PathVariable String appointmentNo) {

        boolean cancelled =
                appointmentService.cancelAppointment(appointmentNo);

        if (!cancelled) {
            return ResponseEntity
                    .badRequest()
                    .body("Unable to cancel appointment: " + appointmentNo);
        }

        return ResponseEntity.ok(
                "Appointment " + appointmentNo +
                        " cancelled successfully"
        );
    }
}

