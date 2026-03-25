package com.ruralHealth.medicalHistory.service;

import com.ruralHealth.medicalHistory.payload.MedicalHistoryDTO;

import java.util.List;

public interface MedicalHistoryService {

    MedicalHistoryDTO createMedicalHistory(MedicalHistoryDTO medicalHistoryDTO);
    MedicalHistoryDTO getMedicalHistoryById(Long id);
    List<MedicalHistoryDTO> getMedicalHistoryByPatientId(Long patientId);
    List<MedicalHistoryDTO> getMedicalHistoryByDoctorId(Long doctorId);
}
