package com.ruralHealth.patient.service;

import com.ruralHealth.entity.User;
import com.ruralHealth.patient.payload.PatientDTORequest;

public interface PatientService {
    public PatientDTORequest savePatient(PatientDTORequest patientDTORequest, User user);
}
