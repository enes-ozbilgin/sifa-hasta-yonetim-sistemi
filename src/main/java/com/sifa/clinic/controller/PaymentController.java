package com.sifa.clinic.controller;

import com.sifa.clinic.model.Payment;
import com.sifa.clinic.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/calculate")
    public ResponseEntity<Map<String, Double>> calculateFinalFee(
            @RequestParam Long appointmentId,
            @RequestParam String tcNo,
            @RequestParam Double baseFee) {
        return ResponseEntity.ok(paymentService.calculateFinalFee(appointmentId, tcNo, baseFee));
    }

    @PostMapping
    public ResponseEntity<Payment> processPayment(@RequestBody Payment payment) {
        return ResponseEntity.ok(paymentService.processPayment(payment));
    }
}