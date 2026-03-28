package com.ruralHealth.vaccination.service;

import com.ruralHealth.vaccination.payload.VaccinationDTO;
import com.ruralHealth.vaccination.payload.VaccinationResponse;

import java.time.LocalDate;
import java.util.List;

public interface VaccinationService {

    VaccinationResponse createVaccine(VaccinationDTO vaccinationDTO);

    List<VaccinationResponse> getRecordByPatient(Long patientId);

    void sendVaccinationReminders();

    List<VaccinationDTO> getDueVaccinations(LocalDate date);

    VaccinationDTO updateNextDose(Long vaccinationId, LocalDate newDate);
}
