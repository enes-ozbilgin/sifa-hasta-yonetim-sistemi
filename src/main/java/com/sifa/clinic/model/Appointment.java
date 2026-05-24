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
public class Appointment {
    
    @Id // Bu alanın Primary Key (Birincil Anahtar) olduğunu belirtir
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID'nin veritabanı tarafından otomatik artırılmasını sağlar
    private Long id;
    
    private Long patientId;
    private Long doctorId;
    private LocalDateTime dateTime;
    
    @Enumerated(EnumType.STRING) // Enum değerlerinin veritabanına sayı (0,1,2) yerine metin (SCHEDULED) olarak kaydedilmesini sağlar
    private AppointmentStatus status; 

    public enum AppointmentStatus {
        SCHEDULED, // Bekliyor
        COMPLETED, // Muayene Edildi
        PAID,      // Ödemesi Alındı
        CANCELED   // İptal Edildi
    }
}