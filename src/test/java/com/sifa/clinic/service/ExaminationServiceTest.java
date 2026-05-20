package com.sifa.clinic.service;

import com.sifa.clinic.model.Appointment;
import com.sifa.clinic.model.Examination;
import com.sifa.clinic.repository.AppointmentRepository;
import com.sifa.clinic.repository.ExaminationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExaminationServiceTest {

    @Mock
    private ExaminationRepository examinationRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private ExaminationService examinationService;

    @Test
    void createExamination_ShouldSaveAndCompleteAppointment_WhenValid() {
        // Hazırlık
        Appointment mockApp = new Appointment();
        mockApp.setId(10L);
        mockApp.setStatus(Appointment.AppointmentStatus.SCHEDULED);

        Examination mockExam = new Examination(1L, 10L, "Grip", "İlaç yazıldı", null);

        when(appointmentRepository.findById(10L)).thenReturn(Optional.of(mockApp));
        when(examinationRepository.save(any(Examination.class))).thenReturn(mockExam);

        // Aksiyon
        Examination savedExam = examinationService.createExamination(mockExam);

        // Doğrulama
        assertNotNull(savedExam);
        assertEquals("Grip", savedExam.getDiagnosis());
        assertEquals(Appointment.AppointmentStatus.COMPLETED, mockApp.getStatus()); // Randevu tamamlandıya dönmüş mü?
        verify(appointmentRepository, times(1)).save(mockApp);
    }

    @Test
    void createExamination_ShouldThrowException_WhenAppointmentNotFound() {
        Examination mockExam = new Examination(1L, 99L, "Grip", "İlaç", null);
        
        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            examinationService.createExamination(mockExam);
        });

        assertEquals("Böyle bir randevu bulunamadı!", ex.getMessage());
    }
}