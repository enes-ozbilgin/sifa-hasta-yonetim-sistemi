package com.sifa.clinic.repository;

import com.sifa.clinic.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // DİYAGRAM 2 (Adım 9): Çift ödeme kontrolü için
    Optional<Payment> findByAppointmentId(Long appointmentId);
}