package com.sifa.clinic.service;

import com.sifa.clinic.model.Appointment;
import com.sifa.clinic.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void createAppointment_ShouldThrowException_WhenDateIsInThePast() {
        Appointment appointment = new Appointment();
        appointment.setDoctorId(1L);
        
        // Geçmiş bir tarih veriyoruz ama dakikasını 00 yapıyoruz ki "30 dakika blok" hatasına takılmasın
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1).withMinute(0).withSecond(0).withNano(0);
        appointment.setDateTime(pastDate);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentService.createAppointment(appointment);
        });

        assertEquals("Geçmiş bir tarihe randevu oluşturulamaz!", exception.getMessage());
    }

    @Test
    void createAppointment_ShouldThrowException_WhenDoctorIsBooked() {
        // Gelecekte ve tam 30 geçe olan kurallara uygun bir saat seçiyoruz
        LocalDateTime futureDate = LocalDateTime.now().plusDays(2).withMinute(30).withSecond(0).withNano(0);
        Appointment appointment = new Appointment(1L, 100L, 1L, futureDate, Appointment.AppointmentStatus.SCHEDULED);

        // DÜZELTME BURADA: Yeni metot ismini ve durum (SCHEDULED) parametresini kullanıyoruz (Diyagram 1)
        when(appointmentRepository.existsByDoctorIdAndDateTimeAndStatus(
                1L, futureDate, Appointment.AppointmentStatus.SCHEDULED)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentService.createAppointment(appointment);
        });

        // Beklenen hata mesajı servis ile birebir eşleşmeli
        assertEquals("Bu saatte doktorun başka bir randevusu var, lütfen başka bir saat seçin!", exception.getMessage());
    }
}