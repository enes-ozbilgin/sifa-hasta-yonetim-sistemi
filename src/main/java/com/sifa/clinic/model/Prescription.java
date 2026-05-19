package com.sifa.clinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {
    private Long id;
    private Long examinationId;
    private String medications; // İlaç listesi (JSON veya virgülle ayrılmış string olabilir)
}