package com.sifa.clinic.service;

import com.sifa.clinic.model.Patient;
import com.sifa.clinic.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient samplePatient;

    @BeforeEach
    void setUp() {
        samplePatient = new Patient(1L, "12345678901", "Ahmet", "Yılmaz", "5551234567", "SGK123");
    }

    @Test
    void createPatient_ShouldSave_WhenTcNoDoesNotExist() {
        // Senaryo: TC No veritabanında yok (null dönüyor)
        when(patientRepository.findByTcNo(samplePatient.getTcNo())).thenReturn(null);
        when(patientRepository.save(any(Patient.class))).thenReturn(samplePatient);

        // Aksiyon
        Patient savedPatient = patientService.createPatient(samplePatient);

        // Doğrulama
        assertNotNull(savedPatient);
        assertEquals("Ahmet", savedPatient.getName());
        verify(patientRepository, times(1)).save(samplePatient);
    }

    @Test
    void createPatient_ShouldThrowException_WhenTcNoExists() {
        // Senaryo: TC No veritabanında zaten kayıtlı
        when(patientRepository.findByTcNo(samplePatient.getTcNo())).thenReturn(samplePatient);

        // Aksiyon ve Doğrulama
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            patientService.createPatient(samplePatient);
        });

        assertEquals("Bu TC Kimlik numarası ile sistemde zaten bir kayıt mevcut!", exception.getMessage());
        verify(patientRepository, never()).save(any(Patient.class)); // save metodu HİÇ çağrılmamalı
    }
}