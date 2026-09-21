package com.sunrisedentalclinic;

import com.sunrisedentalclinic.dao.AppointmentDAO;
import com.sunrisedentalclinic.model.Appointment;
import com.sunrisedentalclinic.model.Dentist;
import com.sunrisedentalclinic.model.Patient;
import com.sunrisedentalclinic.model.Treatment;
import com.sunrisedentalclinic.service.AppointmentService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentServiceTest {

    private final AppointmentService appointmentService =
            new AppointmentService();

    private final AppointmentDAO appointmentDAO =
            new AppointmentDAO();

    @Test
    void testEmptyAppointmentNumber() {

        Appointment appointment = new Appointment(
                "",
                null,
                null,
                null,
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                "SCHEDULED",
                "Test appointment"
        );

        assertFalse(
                appointmentService.registerAppointment(appointment)
        );
    }

    @Test
    void testNullAppointment() {

        assertFalse(
                appointmentService.registerAppointment(null)
        );
    }

    @Test
    void testSearchWithEmptyAppointmentNumber() {

        Appointment appointment =
                appointmentService.searchAppointment("");

        assertNull(appointment);
    }

    @Test
    void testSearchWithUnknownAppointmentNumber() {

        Appointment appointment =
                appointmentService.searchAppointment(
                        "UNKNOWN-APPOINTMENT-999");

        assertNull(appointment);
    }

    @Test
    void testGetAppointmentsByNullDate() {

        assertTrue(
                appointmentService.getAppointmentsByDate(null).isEmpty()
        );
    }

    @Test
    void testCancelWithEmptyAppointmentNumber() {

        assertFalse(
                appointmentService.cancelAppointment("")
        );
    }

    @Test
    void testUpdateWithNullAppointment() {

        assertFalse(
                appointmentService.updateAppointment(null)
        );
    }
}