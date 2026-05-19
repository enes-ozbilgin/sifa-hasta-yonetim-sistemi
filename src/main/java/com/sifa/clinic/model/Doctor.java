package com.sifa.clinic.model;

public class Doctor {
    private Long id;
    private String name;
    private String specialty; // Uzmanlık alanı (Dahiliye, KBB vb.)
    
    // Getter, Setter, Constructor...
<<<<<<< HEAD
=======
    
    public Doctor(Long id, String name, String specialty) {
		super();
		this.id = id;
		this.name = name;
		this.specialty = specialty;
	}
    
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	@Override
	public String toString() {
		return "Doctor [id=" + id + ", name=" + name + ", specialty=" + specialty + "]";
	}
    
    
    
    
>>>>>>> meryem
}
