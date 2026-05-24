package com.sifa.clinic.service;

import com.sifa.clinic.model.Appointment;
import com.sifa.clinic.model.Payment;
import com.sifa.clinic.repository.AppointmentRepository;
import com.sifa.clinic.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private SskIntegrationService sskIntegrationService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Payment testPayment;
    private Appointment testAppointment;

    @BeforeEach
    void setUp() {
        testPayment = new Payment();
        testPayment.setAppointmentId(1L);
        testPayment.setAmount(500.0);
        testPayment.setDiscount(100.0);

        testAppointment = new Appointment();
        testAppointment.setId(1L);
        testAppointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
    }

    @Test
    void processPayment_ShouldThrowException_WhenPaymentAlreadyExists() {
        // Senaryo: Veritabanında zaten bu randevuya ait bir ödeme var
        when(paymentRepository.findByAppointmentId(1L)).thenReturn(Optional.of(testPayment));

        // Test: Tekrar ödeme alınmaya çalışıldığında RuntimeException fırlatılmalı
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentService.processPayment(testPayment);
        });

        assertEquals("Çift Ödeme Hatası: Bu randevu için zaten ödeme alınmış!", exception.getMessage());
        
        // Veritabanına hiçbir kaydetme işlemi yapılmadığını doğrula
        verify(paymentRepository, never()).save(any());
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void processPayment_ShouldSavePaymentAndSetAppointmentToPaid_WhenSuccessful() {
        // Senaryo: Ödeme yok, randevu mevcut
        when(paymentRepository.findByAppointmentId(1L)).thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));

        // İşlemi çalıştır
        Payment savedPayment = paymentService.processPayment(testPayment);

        // Doğrulamalar (Assertions)
        assertNotNull(savedPayment.getPaidAt());
        
        // En kritik test: Randevunun durumu gerçekten PAID olarak değiştirildi mi?
        assertEquals(Appointment.AppointmentStatus.PAID, testAppointment.getStatus());
        
        // Kaydetme metotlarının tetiklendiğini doğrula
        verify(paymentRepository, times(1)).save(testPayment);
        verify(appointmentRepository, times(1)).save(testAppointment);
    }
}