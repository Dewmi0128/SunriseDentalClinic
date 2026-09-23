package com.sunrisedentalclinic.controller;

import com.sunrisedentalclinic.model.Appointment;
import com.sunrisedentalclinic.service.AppointmentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController() {
        this.appointmentService = new AppointmentService();
    }

    @GetMapping("/{appointmentNo}")
    public Appointment getAppointment(
            @PathVariable String appointmentNo) {

        return appointmentService.searchAppointment(appointmentNo);
    }
}