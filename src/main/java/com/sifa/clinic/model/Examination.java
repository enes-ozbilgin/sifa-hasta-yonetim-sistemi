package com.sifa.clinic.model;

public class Examination {
    private Long id;
    private Long appointmentId; // Hangi randevunun muayenesi?
    private String diagnosis;   // Teşhis
    private String treatment;   // Tedavi yöntemi
    private String notes;       // Doktorun özel notları
    
    // Getter, Setter, Constructor...

	public Examination(Long id, Long appointmentId, String diagnosis, String treatment, String notes) {
		super();
		this.id = id;
		this.appointmentId = appointmentId;
		this.diagnosis = diagnosis;
		this.treatment = treatment;
		this.notes = notes;
	}
    
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public Long getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String getTreatment() {
		return treatment;
	}
	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}


	@Override
	public String toString() {
		return "Examination [id=" + id + ", appointmentId=" + appointmentId + ", diagnosis=" + diagnosis
				+ ", treatment=" + treatment + ", notes=" + notes + "]";
	}
	
	
	
    
    
    
    
}