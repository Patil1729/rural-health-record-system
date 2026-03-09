package com.ruralHealth.patient.service;

import com.ruralHealth.entity.User;
import com.ruralHealth.patient.payload.PatientDTORequest;

import java.util.List;

public interface PatientService {
    public PatientDTORequest savePatient(PatientDTORequest patientDTORequest, User user);

    List<PatientDTORequest> getAllPatients();
}
