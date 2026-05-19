package com.sifa.clinic.repository;

import com.sifa.clinic.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    // Spring bu isme bakarak arka planda şu SQL'i yazar:
    // SELECT * FROM appointment WHERE doctor_id = ? AND date_time = ? AND status = 'SCHEDULED'
    boolean existsByDoctorIdAndDateTimeAndStatus(Long doctorId, LocalDateTime dateTime, Appointment.AppointmentStatus status);
    
    // Bir hastanın geçmiş veya gelecek tüm randevularını listelemek için
    List<Appointment> findByPatientId(Long patientId);
}