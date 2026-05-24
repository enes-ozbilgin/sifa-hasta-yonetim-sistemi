package com.sifa.clinic.controller;

import com.sifa.clinic.model.AppUser;
import com.sifa.clinic.model.Payment;
import com.sifa.clinic.model.Role;
import com.sifa.clinic.model.SystemSettings;
import com.sifa.clinic.repository.PaymentRepository;
import com.sifa.clinic.repository.SystemSettingsRepository;
import com.sifa.clinic.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private SystemSettingsRepository settingsRepository;

    @InjectMocks
    private AdminController adminController;

    @Test
    void getDashboardReports_ShouldCalculateStatsCorrectly() {
        // Senaryo: Sistemde 2 Hasta, 1 Doktor var
        AppUser p1 = new AppUser(); p1.setRole(Role.PATIENT);
        AppUser p2 = new AppUser(); p2.setRole(Role.PATIENT);
        AppUser d1 = new AppUser(); d1.setRole(Role.DOCTOR);
        
        when(userRepository.findAll()).thenReturn(Arrays.asList(p1, p2, d1));

        // Senaryo: Bu ay yapılmış 2 geçerli ödeme var (biri 500TL, diğeri 300TL)
        Payment pay1 = new Payment(); pay1.setAmount(600.0); pay1.setDiscount(100.0); pay1.setPaidAt(LocalDateTime.now());
        Payment pay2 = new Payment(); pay2.setAmount(300.0); pay2.setDiscount(0.0); pay2.setPaidAt(LocalDateTime.now());
        
        when(paymentRepository.findAll()).thenReturn(Arrays.asList(pay1, pay2));

        // İşlemi çalıştır
        ResponseEntity<DashboardResponse> response = adminController.getDashboardReports();

        // Doğrulamalar (Beklenen istatistiklerin doğru döndüğünden emin ol)
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getPatientCount());
        assertEquals(1, response.getBody().getDoctorCount());
        assertEquals(0, response.getBody().getCashierCount()); // Veznedar eklemediğimiz için 0 olmalı
        assertEquals(800.0, response.getBody().getMonthlyRevenue()); // 500 + 300
    }

    @Test
    void updateSettings_ShouldAlwaysSaveToIdOne() {
        // Senaryo: Admin panelinden yeni ayarlar geliyor
        SystemSettings newSettings = new SystemSettings();
        newSettings.setAppointmentSlotMinutes(45);
        newSettings.setDiscountRate(0.25);
        
        when(settingsRepository.save(any(SystemSettings.class))).thenReturn(newSettings);

        // İşlemi çalıştır
        ResponseEntity<SystemSettings> response = adminController.updateSettings(newSettings);

        // Doğrulama: Kaydedilen ID her zaman 1 olmalı ki veritabanı şişmesin
        assertEquals(1L, newSettings.getId());
        verify(settingsRepository, times(1)).save(newSettings);
    }

    @Test
    void deleteUser_ShouldCallRepositoryDelete() {
        // İşlemi çalıştır
        ResponseEntity<Void> response = adminController.deleteUser(5L);

        // Doğrulama
        assertEquals(200, response.getStatusCode().value());
        verify(userRepository, times(1)).deleteById(5L);
    }
}