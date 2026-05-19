package com.sifa.clinic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AppUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username; // TC Kimlik No veya Email olabilir
    
    @Column(nullable = false)
    private String password; // Şifre (Veritabanında şifrelenmiş duracak)
    
    @Enumerated(EnumType.STRING)
    private Role role; // Kişinin yetkisi
}