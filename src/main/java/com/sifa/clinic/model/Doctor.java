package com.sifa.clinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    private Long id;
    private String name;
    private String specialty; // Uzmanlık alanı (Dahiliye, KBB vb.)
}