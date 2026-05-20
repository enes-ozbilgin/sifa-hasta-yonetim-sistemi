package com.sifa.clinic.controller;

import com.sifa.clinic.model.Payment;
import com.sifa.clinic.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // Sadece VEZNE (CASHIER) rolü erişebilir
    // POST http://localhost:8080/api/payments
    @PostMapping
    public ResponseEntity<Payment> makePayment(@RequestBody Payment payment) {
        Payment processedPayment = paymentService.processPayment(payment);
        return new ResponseEntity<>(processedPayment, HttpStatus.CREATED);
    }
}