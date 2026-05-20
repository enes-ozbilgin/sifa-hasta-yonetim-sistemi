package com.sifa.clinic.controller;

import com.sifa.clinic.model.Examination;
import com.sifa.clinic.service.ExaminationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/examinations")
@RequiredArgsConstructor
public class ExaminationController {

    private final ExaminationService examinationService;

    // Yeni muayene kaydı oluştur (Sadece Doktorlar erişebilir)
    // POST http://localhost:8080/api/examinations
    @PostMapping
    public ResponseEntity<Examination> createExamination(@RequestBody Examination examination) {
        Examination savedExam = examinationService.createExamination(examination);
        return new ResponseEntity<>(savedExam, HttpStatus.CREATED);
    }
}