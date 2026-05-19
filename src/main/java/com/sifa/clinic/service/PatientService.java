package com.sifa.clinic.service;

import com.sifa.clinic.model.Patient;
import com.sifa.clinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    
    // Lombok'un @RequiredArgsConstructor'ı sayesinde Constructor yazmadan Dependency Injection yapıyoruz.
    private final PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Transactional // İşlem sırasında hata çıkarsa veritabanını geri almak (rollback) için
    public Patient createPatient(Patient patient) {
        // Yeni hasta eklerken TC Kimlik kontrolü
        if (patientRepository.findByTcNo(patient.getTcNo()) != null) {
            throw new RuntimeException("Bu TC Kimlik numarası ile sistemde zaten bir kayıt mevcut!");
        }
        return patientRepository.save(patient);
    }
}