package com.sifa.clinic.service;

import com.sifa.clinic.model.Appointment;
import com.sifa.clinic.model.Payment;
import com.sifa.clinic.repository.AppointmentRepository;
import com.sifa.clinic.repository.PaymentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final SskIntegrationService sskIntegrationService;
    private final AppointmentRepository appointmentRepository; // YENİ EKLENDİ

    public Map<String, Double> calculateFinalFee(Long appointmentId, String tcNo, Double baseFee) {
        double discountRate = sskIntegrationService.getDiscountRate(tcNo);
        double discountAmount = baseFee * discountRate;
        double finalFee = baseFee - discountAmount;
        
        Map<String, Double> result = new HashMap<>();
        result.put("discountAmount", discountAmount);
        result.put("finalFee", finalFee);
        return result;
    }
    
    @Transactional // YENİ EKLENDİ: İşlem güvenliği için
    public Payment processPayment(Payment payment) {
        Optional<Payment> existingPayment = paymentRepository.findByAppointmentId(payment.getAppointmentId());
        
        if (existingPayment.isPresent()) {
            throw new RuntimeException("Çift Ödeme Hatası: Bu randevu için zaten ödeme alınmış!");
        }

        payment.setPaidAt(LocalDateTime.now());
        Payment savedPayment = paymentRepository.save(payment);

        Appointment appointment = appointmentRepository.findById(payment.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı!"));
        
        appointment.setStatus(Appointment.AppointmentStatus.PAID);
        appointmentRepository.save(appointment);

        return savedPayment;
    }
}