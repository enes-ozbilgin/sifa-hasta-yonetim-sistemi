package com.sifa.clinic.service;

import com.sifa.clinic.model.Appointment;
import com.sifa.clinic.model.Examination;
import com.sifa.clinic.repository.AppointmentRepository;
import com.sifa.clinic.repository.ExaminationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExaminationService {

    private final ExaminationRepository examinationRepository;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public Examination createExamination(Examination examination) {
        // 1. Randevu gerçekten var mı kontrol et
        Appointment appointment = appointmentRepository.findById(examination.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Böyle bir randevu bulunamadı!"));

        // 2. Muayene yapıldığına göre randevunun durumunu "COMPLETED" (Tamamlandı) yap
        appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        // 3. Muayene kaydını veritabanına kaydet
        return examinationRepository.save(examination);
    }
}