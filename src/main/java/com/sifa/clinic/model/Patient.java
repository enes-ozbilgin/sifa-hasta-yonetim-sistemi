package com.sifa.clinic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
	
	@Id // Bu alanın Primary Key (Birincil Anahtar) olduğunu belirtir
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID'nin veritabanı tarafından otomatik artırılmasını sağlar
    private Long id;
	
    private String tcNo;
    private String name;
    private String surname;
    private String phone;
    private String insuranceNo; // Sigorta numarası boş olabilir (SGK'sız hasta)
}