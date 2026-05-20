package com.sifa.clinic.controller;

import com.sifa.clinic.model.Examination;
import com.sifa.clinic.service.ExaminationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExaminationControllerTest {

    @Mock
    private ExaminationService examinationService;

    @InjectMocks
    private ExaminationController examinationController;

    @Test
    void createExamination_ShouldReturnCreated() {
        Examination mockExam = new Examination(1L, 10L, "Migren", "Ağrı Kesici", "Karanlık oda");
        when(examinationService.createExamination(any(Examination.class))).thenReturn(mockExam);

        ResponseEntity<Examination> response = examinationController.createExamination(mockExam);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Migren", response.getBody().getDiagnosis());
    }
}