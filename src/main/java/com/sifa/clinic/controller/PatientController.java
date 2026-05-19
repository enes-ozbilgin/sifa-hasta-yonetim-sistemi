package com.sifa.clinic.controller;

import com.sifa.clinic.model.Patient;
import com.sifa.clinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    // GET isteği: http://localhost:8080/api/patients
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    // POST isteği: http://localhost:8080/api/patients
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.createPatient(patient);
        return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
    }
}