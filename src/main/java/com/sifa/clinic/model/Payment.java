package com.sifa.clinic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long appointmentId;
    private Double amount;
    private Double discount;
    private String paymentMethod;
    
    // DİYAGRAM 2: Ödeme zamanı eklendi
    private LocalDateTime paidAt; 
}