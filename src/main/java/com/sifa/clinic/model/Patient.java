package com.sifa.clinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    private Long id;
    private String tcNo;
    private String name;
    private String surname;
    private String phone;
    private String insuranceNo; // Sigorta numarası boş olabilir (SGK'sız hasta)
}