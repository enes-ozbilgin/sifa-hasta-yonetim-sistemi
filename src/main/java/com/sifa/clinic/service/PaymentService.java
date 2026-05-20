package com.sifa.clinic.service;

import com.sifa.clinic.model.Payment;
import com.sifa.clinic.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment processPayment(Payment payment) {
        // 1. Bu randevuya ait ödeme zaten yapılmış mı?
        if (paymentRepository.findByAppointmentId(payment.getAppointmentId()).isPresent()) {
            throw new RuntimeException("Bu randevu için zaten bir ödeme alınmış!");
        }

        // 2. Ödeme zamanını şu anki saat olarak ayarla
        payment.setPaidAt(LocalDateTime.now());

        // 3. Sigorta indirimi varsa (null değilse), toplam tutardan düşebilirsin vs.
        // Bu örnekte doğrudan veritabanına kaydediyoruz
        return paymentRepository.save(payment);
    }
}