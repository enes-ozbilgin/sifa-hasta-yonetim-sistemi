package com.sifa.clinic.repository;

import com.sifa.clinic.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Bir randevunun ödemesi daha önce yapılmış mı diye kontrol etmek için
    Optional<Payment> findByAppointmentId(Long appointmentId);
}