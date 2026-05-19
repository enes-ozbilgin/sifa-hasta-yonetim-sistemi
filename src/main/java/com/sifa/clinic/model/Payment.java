package com.sifa.clinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private Long id;
    private Long appointmentId;
    private BigDecimal amount;            // Toplam tutar
    private BigDecimal insuranceDiscount; // Sigorta indirimi
    private PaymentMethod method;         // NAKIT, KREDI_KARTI vb.
    private LocalDateTime paidAt;         // Ödeme zamanı
    
    public enum PaymentMethod {
        CASH,
        CREDIT_CARD,
        INSURANCE_COVERED // Tamamı sigortadan karşılanmışsa
    }
}