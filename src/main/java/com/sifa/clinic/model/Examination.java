package com.sifa.clinic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Veritabanı tablosu olduğunu belirtir
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Examination {
    
    @Id // Birincil anahtar (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Otomatik artan ID (1, 2, 3...)
    private Long id;
    
    private Long appointmentId; // Hangi randevunun muayenesi?
    private String diagnosis;   // Teşhis
    private String treatment;   // Tedavi yöntemi
    private String notes;       // Doktorun özel notları
}