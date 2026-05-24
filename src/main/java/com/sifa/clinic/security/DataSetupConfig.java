package com.sifa.clinic.security;

import com.sifa.clinic.model.AppUser;
import com.sifa.clinic.model.Role;
import com.sifa.clinic.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSetupConfig {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Eğer veritabanında "admin" isminde biri yoksa otomatik oluştur
            if (userRepository.findByUsername("admin").isEmpty()) {
                AppUser admin = new AppUser();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123")); // Şifre: admin123
                admin.setRole(Role.ADMIN);
                
                userRepository.save(admin);
                System.out.println("--> [SİSTEM] Varsayılan Admin hesabı başarıyla oluşturuldu: admin / admin123");
            }
        };
    }
}