package com.sifa.clinic.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    private Long id;
    private Long appointmentId;
    private BigDecimal amount;            // Toplam tutar
    private BigDecimal insuranceDiscount; // Sigorta indirimi
    private PaymentMethod method;         // NAKIT, KREDI_KARTI vb.
    private LocalDateTime paidAt;         // Ödeme zamanı
    
    // Getter, Setter, Constructor...

    public enum PaymentMethod {
        CASH,
        CREDIT_CARD,
        INSURANCE_COVERED // Tamamı sigortadan karşılanmışsa
    }
}