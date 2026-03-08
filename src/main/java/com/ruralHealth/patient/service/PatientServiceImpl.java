package com.ruralHealth.patient.service;

import com.ruralHealth.entity.Patient;
import com.ruralHealth.entity.User;
import com.ruralHealth.exception.ApiException;
import com.ruralHealth.patient.payload.PatientDTORequest;
import com.ruralHealth.repository.PatientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PatientRepository patientRepository;

    @Override
    public PatientDTORequest savePatient(PatientDTORequest patientDTORequest, User user) {

        Patient patientToSave = modelMapper.map(patientDTORequest, Patient.class);
        Patient savedPatient = patientRepository.findByName(patientToSave.getName());

        if(savedPatient!=null){
            throw new ApiException("Patient with name "+patientToSave.getName()+" already exists");
        }
        patientToSave.setCreatedBy(user);
        Patient savedPatientInDB = patientRepository.save(patientToSave);

        PatientDTORequest response = modelMapper.map(savedPatientInDB, PatientDTORequest.class);
        if(user!=null){
            response.setCreatedBy(user.getUserName());
        }

        return response;
    }
}
