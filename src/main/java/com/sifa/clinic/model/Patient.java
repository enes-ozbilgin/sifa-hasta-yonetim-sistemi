package com.sifa.clinic.model;

public class Patient {
    private Long id;
    private String tcNo;
    private String name;
    private String surname;
    private String phone;
    private String insuranceNo; // Sigorta numarası boş olabilir (SGK'sız hasta)
	
    
    // Getter, Setter, Constructor...
    
	public Patient(Long id, String tcNo, String name, String surname, String phone, String insuranceNo) {
		super();
		this.id = id;
		this.tcNo = tcNo;
		this.name = name;
		this.surname = surname;
		this.phone = phone;
		this.insuranceNo = insuranceNo;
	}
    
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getTcNo() {
		return tcNo;
	}
	public void setTcNo(String tcNo) {
		this.tcNo = tcNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getInsuranceNo() {
		return insuranceNo;
	}
	public void setInsuranceNo(String insuranceNo) {
		this.insuranceNo = insuranceNo;
	}


	@Override
	public String toString() {
		return "Patient [id=" + id + ", tcNo=" + tcNo + ", name=" + name + ", surname=" + surname + ", phone=" + phone
				+ ", insuranceNo=" + insuranceNo + "]";
	}
	
	
    
}