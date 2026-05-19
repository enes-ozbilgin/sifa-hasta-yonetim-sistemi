package com.sifa.clinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private LocalDateTime dateTime;
    private AppointmentStatus status; 

    public enum AppointmentStatus {
        SCHEDULED,  // Planlandı
        COMPLETED,  // Tamamlandı
        CANCELED    // İptal Edildi
    }
}