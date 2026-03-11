package com.ruralHealth.patient.service;

import com.ruralHealth.entity.User;
import com.ruralHealth.patient.payload.PatientDTORequest;

import java.util.List;

public interface PatientService {
    public PatientDTORequest savePatient(PatientDTORequest patientDTORequest, User user);

    List<PatientDTORequest> getAllPatients();

    PatientDTORequest searchPatientById(Long id);

    PatientDTORequest searchPatientByPhoneNumber(String phoneNumber);

    List<PatientDTORequest> searchPatientByName(String name);

    List<PatientDTORequest> getAllPatientsByLoggedInUser(User user);

    List<PatientDTORequest> searchPatientsByVillageName(String villageName);

    List<PatientDTORequest> searchPatientsByDiseaseName(String diseaseName);

    PatientDTORequest updatePatient(PatientDTORequest patientDTORequest, Long id);

    PatientDTORequest deletePatient(Long id);
}
