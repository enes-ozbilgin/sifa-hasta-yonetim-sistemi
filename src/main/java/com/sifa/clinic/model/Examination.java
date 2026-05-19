package com.sifa.clinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Examination {
    private Long id;
    private Long appointmentId; // Hangi randevunun muayenesi?
    private String diagnosis;   // Teşhis
    private String treatment;   // Tedavi yöntemi
    private String notes;       // Doktorun özel notları
}