package com.sifa.clinic.service;

import com.sifa.clinic.model.Payment;
import com.sifa.clinic.repository.PaymentRepository;
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
    private SskIntegrationService sskIntegrationService; // YENİ EKLENDİ (Diyagram 2)

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void calculateFinalFee_ShouldApplyDiscount_WhenTcIsValid() {
        // Hazırlık: SSK servisinden (Mock) %20 indirim dönmesini bekliyoruz
        when(sskIntegrationService.getDiscountRate("11122233344")).thenReturn(0.20);

        // Aksiyon: 1000 TL taban ücret ile servisi çağır
        Map<String, Double> result = paymentService.calculateFinalFee(5L, "11122233344", 1000.0);

        // Doğrulama: 1000 TL'nin %20'si 200 TL indirim, net 800 TL kalmalı
        assertEquals(200.0, result.get("discountAmount"));
        assertEquals(800.0, result.get("finalFee"));
    }

    @Test
    void processPayment_ShouldSave_WhenNoPriorPaymentExists() {
        // BigDecimal yerine Double kullanıyoruz çünkü modelimizi öyle güncelledik
        Payment payment = new Payment(null, 5L, 500.0, 0.0, "CREDIT_CARD", null); 
        
        when(paymentRepository.findByAppointmentId(5L)).thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.processPayment(payment);

        assertNotNull(result.getPaidAt()); // Tarih başarıyla atanmış mı?
        verify(paymentRepository, times(1)).save(payment); // Veritabanına kayıt atılmış mı?
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

        // Hata mesajı servisimizdeki güncel mesajla birebir aynı olmalı
        assertEquals("Çift Ödeme Hatası: Bu randevu için zaten ödeme alınmış!", ex.getMessage());
    }
}