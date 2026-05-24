package com.sifa.clinic.service;

import com.sifa.clinic.model.Appointment;
import com.sifa.clinic.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    // EKSİK OLAN METOT BURASI: Hastanın randevularını veritabanından getirir
    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }
    
    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }
    
 // Veznedarın tüm randevuları görebilmesi için eklendi
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    
    public Appointment createAppointment(Appointment appointment) {
        // YENİ KURAL: Geçmiş bir tarihe randevu alınamaz
        if (appointment.getDateTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Geçmiş bir tarihe randevu oluşturulamaz!");
        }

        // KURAL 1: Sadece 00 veya 30 geçe (30 dakikalık bloklar) randevu alınabilir
        int minute = appointment.getDateTime().getMinute();
        if (minute != 0 && minute != 30) {
            throw new RuntimeException("Randevular sadece 30 dakikalık bloklar halinde (00 veya 30 geçe) verilebilir!");
        }

        // KURAL 2: Aynı doktora aynı saatte AKTİF (SCHEDULED) başka randevu var mı?
        if (appointmentRepository.existsByDoctorIdAndDateTimeAndStatus(
                appointment.getDoctorId(), 
                appointment.getDateTime(), 
                Appointment.AppointmentStatus.SCHEDULED)) {
            throw new RuntimeException("Bu saatte doktorun başka bir randevusu var, lütfen başka bir saat seçin!");
        }

        return appointmentRepository.save(appointment);
    }
}