package com.sifa.clinic.service;

import org.springframework.stereotype.Service;

@Service
public class SskIntegrationService {

    // DİYAGRAM 2: Dış Sistem (SSK API) Entegrasyonu Taklidi
    public double getDiscountRate(String tcNo) {
        // Gerçek bir projede burada E-Devlet veya SGK sunucularına istek atılır.
        // Biz burada basit bir iş kuralı (Mock) yazıyoruz:
        // Eğer TC Kimlik No verilmişse ve 11 haneliyse %20 indirim tanımla
        if (tcNo != null && tcNo.length() == 11) {
            return 0.20; // %20 İndirim
        }
        return 0.0; // İndirim yok
    }
}