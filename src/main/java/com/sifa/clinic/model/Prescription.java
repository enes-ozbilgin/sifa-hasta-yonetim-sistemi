package com.sifa.clinic.model;

public class Prescription {
    private Long id;
    private Long examinationId;
    private String medications; // İlaç listesi (JSON veya virgülle ayrılmış string olabilir)
<<<<<<< HEAD
    
    // Getter, Setter, Constructor...
=======
	
    // Getter, Setter, Constructor...

    
    public Prescription(Long id, Long examinationId, String medications) {
		super();
		this.id = id;
		this.examinationId = examinationId;
		this.medications = medications;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getExaminationId() {
		return examinationId;
	}

	public void setExaminationId(Long examinationId) {
		this.examinationId = examinationId;
	}

	public String getMedications() {
		return medications;
	}

	public void setMedications(String medications) {
		this.medications = medications;
	}

	@Override
	public String toString() {
		return "Prescription [id=" + id + ", examinationId=" + examinationId + ", medications=" + medications + "]";
	}
    
    
    
>>>>>>> meryem
}