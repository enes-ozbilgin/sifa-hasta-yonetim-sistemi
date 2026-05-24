package com.sifa.clinic.controller;

import com.sifa.clinic.model.AppUser;
import com.sifa.clinic.model.Role;
import com.sifa.clinic.model.SystemSettings;
import com.sifa.clinic.repository.PaymentRepository;
import com.sifa.clinic.repository.SystemSettingsRepository;
import com.sifa.clinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final SystemSettingsRepository settingsRepository;

    // 1. GENEL RAPORLAR (Gelir ve Kullanıcı Sayıları)
    @GetMapping("/reports")
    public ResponseEntity<DashboardResponse> getDashboardReports() {
        List<AppUser> allUsers = userRepository.findAll();
        
        long doctorCount = allUsers.stream().filter(u -> u.getRole() == Role.DOCTOR).count();
        long cashierCount = allUsers.stream().filter(u -> u.getRole() == Role.CASHIER).count();
        long patientCount = allUsers.stream().filter(u -> u.getRole() == Role.PATIENT).count();

        // Sadece bu ay ödenmiş (PAID) olanların net tutarlarını topla
        double monthlyRevenue = paymentRepository.findAll().stream()
                .filter(p -> p.getPaidAt() != null && p.getPaidAt().getMonth() == LocalDateTime.now().getMonth())
                .mapToDouble(p -> p.getAmount() - p.getDiscount())
                .sum();

        return ResponseEntity.ok(new DashboardResponse(doctorCount, cashierCount, patientCount, monthlyRevenue));
    }

    // 2. SİSTEM AYARLARI OKUMA
    @GetMapping("/settings")
    public ResponseEntity<SystemSettings> getSettings() {
        // Ayar yoksa varsayılan olarak 30 dk ve %20 indirim döner
        SystemSettings settings = settingsRepository.findById(1L)
                .orElse(new SystemSettings(1L, 30, 0.20));
        return ResponseEntity.ok(settings);
    }

    // 3. SİSTEM AYARLARI GÜNCELLEME
    @PostMapping("/settings")
    public ResponseEntity<SystemSettings> updateSettings(@RequestBody SystemSettings settings) {
        settings.setId(1L); // Her zaman aynı satırı (ID:1) ezer
        return ResponseEntity.ok(settingsRepository.save(settings));
    }

    // 4. KULLANICI LİSTESİ GETİRME
    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // 5. KULLANICI SİLME
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

// Dikkat: Başında 'public' yok!
@Data
@AllArgsConstructor
class DashboardResponse {
    private long doctorCount;
    private long cashierCount;
    private long patientCount;
    private double monthlyRevenue;
}