package com.sifa.clinic.repository;

import com.sifa.clinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    // Sadece bu satırı yazarak, Spring'in arka planda "SELECT * FROM patient WHERE tc_no = ?" 
    // sorgusunu otomatik üretmesini sağlıyoruz.
    Patient findByTcNo(String tcNo);
}