package com.sifa.clinic.service;

import com.sifa.clinic.model.Appointment;
import com.sifa.clinic.repository.AppointmentRepository;
import com.sifa.clinic.service.AppointmentService;

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
        // Geçmiş tarihe randevu alma senaryosu
        Appointment appointment = new Appointment();
        appointment.setDoctorId(1L);
        appointment.setDateTime(LocalDateTime.now().minusDays(1)); // Dün

        // Aksiyon ve Doğrulama
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentService.createAppointment(appointment);
        });

        assertEquals("Geçmiş bir tarihe randevu oluşturulamaz!", exception.getMessage());
    }

    @Test
    void createAppointment_ShouldThrowException_WhenDoctorIsBooked() {
        // Doktorun o saatte dolu olma senaryosu
        LocalDateTime futureDate = LocalDateTime.now().plusDays(2);
        Appointment appointment = new Appointment(1L, 100L, 1L, futureDate, null);

        when(appointmentRepository.existsByDoctorIdAndDateTimeAndStatus(
                1L, futureDate, Appointment.AppointmentStatus.SCHEDULED)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentService.createAppointment(appointment);
        });

        assertEquals("Seçilen doktorun bu saatte başka bir randevusu bulunmaktadır. Lütfen farklı bir saat seçiniz.", exception.getMessage());
    }
}