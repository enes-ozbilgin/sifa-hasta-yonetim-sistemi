package com.sifa.clinic.controller;

import com.sifa.clinic.model.Patient;
import com.sifa.clinic.service.PatientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// Spring'i tamamen devreden çıkardık, sadece Mockito kullanıyoruz!
@ExtendWith(MockitoExtension.class) 
class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    @Test
    void createPatient_ShouldReturnCreatedStatus() {
        // 1. Hazırlık (Mock Veri)
        Patient mockPatient = new Patient(1L, "11122233344", "Ayşe", "Kaya", "5559998877", null);
        
        // Service'in davranışı belirleniyor
        when(patientService.createPatient(any(Patient.class))).thenReturn(mockPatient);

        // 2. Aksiyon (API isteği atmıyoruz, Controller'daki metodu DOĞRUDAN çağırıyoruz)
        ResponseEntity<Patient> response = patientController.createPatient(mockPatient);

        // 3. Doğrulama (Assert)
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Ayşe", response.getBody().getName());
        assertEquals("11122233344", response.getBody().getTcNo());
    }
}