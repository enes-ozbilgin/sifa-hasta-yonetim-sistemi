package com.sifa.clinic.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Şifreleri veritabanına "12345" gibi açık metin yerine karmakarışık (hashlenmiş) kaydetmemizi sağlar
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // İleride React (Frontend) kullanacağımız için API bazlı çalışıyoruz, CSRF kapalı kalmalı
            .authorizeHttpRequests(auth -> auth
                // Herkesin erişebileceği yerler (Login ve Kayıt olma sayfaları)
                .requestMatchers("/api/auth/**", "/login", "/register").permitAll()
                
                // Sadece DOKTOR rolündekiler muayene işlemlerini görebilir
                .requestMatchers("/api/examinations/**").hasRole("DOCTOR")
                
                // Sadece VEZNE (CASHIER) rolü ödeme işlemlerini yapabilir
                .requestMatchers("/api/payments/**").hasRole("CASHIER")
                
                // Randevuları HASTA ve DOKTOR görebilir/yönetebilir
                .requestMatchers("/api/appointments/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN")
                
                // Diğer kalan TÜM istekler için sisteme giriş yapmış olma (Authenticated) şartı arıyoruz
                .anyRequest().authenticated()
            )
            // Şimdilik test amaçlı Spring'in varsayılan Basic Auth (pencere açılan) girişini kullanıyoruz
            // Merve buraya daha sonra JWT (JSON Web Token) filtresini ekleyecek
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}