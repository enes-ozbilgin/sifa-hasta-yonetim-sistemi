package com.sifa.clinic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemSettings {
    @Id
    private Long id;
    private Integer appointmentSlotMinutes; // Randevu aralığı (örn: 30)
    private Double discountRate;            // İndirim oranı (örn: 0.20)
}