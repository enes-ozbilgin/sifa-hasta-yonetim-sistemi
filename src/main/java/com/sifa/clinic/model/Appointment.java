package com.sifa.clinic.model;
import java.time.LocalDateTime;

public class Appointment {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private LocalDateTime dateTime;
    private AppointmentStatus status; 

    // Getter, Setter, Constructor...
    
    public enum AppointmentStatus {
        SCHEDULED,  // Planlandı
        COMPLETED,  // Tamamlandı
        CANCELED    // İptal Edildi
    }
}