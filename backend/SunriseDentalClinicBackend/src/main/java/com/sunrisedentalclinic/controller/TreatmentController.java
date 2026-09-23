package com.sunrisedentalclinic.controller;

import com.sunrisedentalclinic.dao.TreatmentDAO;
import com.sunrisedentalclinic.model.Treatment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatments")
public class TreatmentController {

    private final TreatmentDAO treatmentDAO;

    public TreatmentController() {
        this.treatmentDAO = new TreatmentDAO();
    }

    @GetMapping
    public ResponseEntity<List<Treatment>> getAllTreatments() {

        return ResponseEntity.ok(treatmentDAO.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTreatmentById(@PathVariable int id) {

        Treatment treatment = treatmentDAO.findById(id);

        if (treatment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(treatment);
    }
}