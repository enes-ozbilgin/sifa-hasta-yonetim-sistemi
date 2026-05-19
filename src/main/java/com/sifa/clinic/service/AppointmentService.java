package com.sifa.clinic.service;

import com.sifa.clinic.model.Appointment;
import com.sifa.clinic.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    @Transactional
    public Appointment createAppointment(Appointment appointment) {
        // 1. Kural: Doktorun o saatte onaylanmış (SCHEDULED) başka bir randevusu var mı?
        boolean isBooked = appointmentRepository.existsByDoctorIdAndDateTimeAndStatus(
                appointment.getDoctorId(), 
                appointment.getDateTime(), 
                Appointment.AppointmentStatus.SCHEDULED
        );

        if (isBooked) {
            throw new RuntimeException("Seçilen doktorun bu saatte başka bir randevusu bulunmaktadır. Lütfen farklı bir saat seçiniz.");
        }

        // 2. Kural: Geçmiş bir tarihe randevu alınamaz
        if (appointment.getDateTime().isBefore(java.time.LocalDateTime.now())) {
            throw new RuntimeException("Geçmiş bir tarihe randevu oluşturulamaz!");
        }

        // Her şey yolundaysa durumu SCHEDULED (Planlandı) yap ve kaydet
        appointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);
        return appointmentRepository.save(appointment);
    }
}