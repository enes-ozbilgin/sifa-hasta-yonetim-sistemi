package com.sifa.clinic.repository;

import com.sifa.clinic.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    
    // EKSİK OLAN METOT: Doktor ID'sine göre randevuları bulma
    List<Appointment> findByDoctorId(Long doctorId);
    
    boolean existsByDoctorIdAndDateTimeAndStatus(Long doctorId, LocalDateTime dateTime, Appointment.AppointmentStatus status);
}