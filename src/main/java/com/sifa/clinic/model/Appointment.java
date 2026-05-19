package com.sifa.clinic.model;
import java.time.LocalDateTime;

public class Appointment {
<<<<<<< HEAD
    private Long id;
=======
	
	private Long id;
>>>>>>> meryem
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
<<<<<<< HEAD
}
=======
    
	
    public Appointment(Long id, Long patientId, Long doctorId, LocalDateTime dateTime, AppointmentStatus status) {
		super();
		this.id = id;
		this.patientId = patientId;
		this.doctorId = doctorId;
		this.dateTime = dateTime;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public AppointmentStatus getStatus() {
		return status;
	}

	public void setStatus(AppointmentStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Appointment [id=" + id + ", patientId=" + patientId + ", doctorId=" + doctorId + ", dateTime="
				+ dateTime + ", status=" + status + "]";
	}
	
	
}



>>>>>>> meryem
