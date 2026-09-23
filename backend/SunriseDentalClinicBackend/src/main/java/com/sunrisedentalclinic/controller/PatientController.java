package com.sunrisedentalclinic.controller;

import com.sunrisedentalclinic.model.Patient;
import com.sunrisedentalclinic.dao.PatientDAO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientDAO patientDAO;

    public PatientController() {
        this.patientDAO = new PatientDAO();
    }

    @PostMapping
    public String registerPatient(@RequestBody Patient patient) {

        if (patient == null) {
            return "Patient data is required";
        }

        boolean saved = patientDAO.save(patient);

        if (!saved) {
            return "Patient registration failed";
        }

        return "Patient registered successfully";
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {

        return ResponseEntity.ok(patientDAO.findAll());
    }

    @GetMapping("/{patientId}")
    public Patient getPatient(@PathVariable int patientId) {

        return patientDAO.findById(patientId);
    }
}