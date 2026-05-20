package com.sifa.clinic.repository;

import com.sifa.clinic.model.Examination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExaminationRepository extends JpaRepository<Examination, Long> {
    // Doktorun daha önce yaptığı tüm muayeneleri randevu ID'si üzerinden bulmak için
    List<Examination> findByAppointmentId(Long appointmentId);
}