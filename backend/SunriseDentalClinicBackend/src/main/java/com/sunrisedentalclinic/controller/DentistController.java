package com.sunrisedentalclinic.controller;

import com.sunrisedentalclinic.dao.DentistDAO;
import com.sunrisedentalclinic.model.Dentist;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dentists")
public class DentistController {

    private final DentistDAO dentistDAO;

    public DentistController() {
        this.dentistDAO = new DentistDAO();
    }

    @GetMapping
    public ResponseEntity<List<Dentist>> getAllDentists() {

        return ResponseEntity.ok(dentistDAO.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDentistById(@PathVariable int id) {

        Dentist dentist = dentistDAO.findById(id);

        if (dentist == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dentist);
    }
}