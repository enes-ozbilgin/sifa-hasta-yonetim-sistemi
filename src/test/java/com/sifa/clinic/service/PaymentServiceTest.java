package com.sifa.clinic.service;

import com.sifa.clinic.model.Payment;
import com.sifa.clinic.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void processPayment_ShouldSave_WhenNoPriorPaymentExists() {
        Payment payment = new Payment(null, 5L, new BigDecimal("500.00"), new BigDecimal("0.00"), Payment.PaymentMethod.CREDIT_CARD, null);
        
        when(paymentRepository.findByAppointmentId(5L)).thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.processPayment(payment);

        assertNotNull(result.getPaidAt()); // Tarih atanmış mı?
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void processPayment_ShouldThrowException_WhenAlreadyPaid() {
        Payment existingPayment = new Payment();
        Payment newPayment = new Payment();
        newPayment.setAppointmentId(5L);

        when(paymentRepository.findByAppointmentId(5L)).thenReturn(Optional.of(existingPayment));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            paymentService.processPayment(newPayment);
        });

        assertEquals("Bu randevu için zaten bir ödeme alınmış!", ex.getMessage());
    }
}