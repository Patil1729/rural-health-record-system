package com.ruralHealth.patient.service;

import com.ruralHealth.entity.Patient;
import com.ruralHealth.entity.User;
import com.ruralHealth.exception.ApiException;
import com.ruralHealth.exception.ResourceNotFoundException;
import com.ruralHealth.patient.payload.PatientDTORequest;
import com.ruralHealth.repository.PatientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PatientRepository patientRepository;


    private PatientDTORequest mapPatientToDTO(Patient patient) {
        PatientDTORequest patientDTO = modelMapper.map(patient, PatientDTORequest.class);
        User createdBy = patient.getCreatedBy();
        if (createdBy != null) {
            patientDTO.setCreatedBy(createdBy.getUserName());
        }
        return patientDTO;
    }

    private List<PatientDTORequest> mapPatientsToDTOs(List<Patient> patients) {
        return patients.stream()
                .map(this::mapPatientToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PatientDTORequest savePatient(PatientDTORequest patientDTORequest, User user) {

        Patient patientToSave = modelMapper.map(patientDTORequest, Patient.class);
        Patient savedPatient = patientRepository.findByName(patientToSave.getName());

        if(savedPatient!=null){
            throw new ApiException("Patient with name "+patientToSave.getName()+" already exists");
        }
        patientToSave.setCreatedBy(user);
        Patient savedPatientInDB = patientRepository.save(patientToSave);

       // PatientDTORequest response = mapPatientToDTO(savedPatientInDB);

        return mapPatientToDTO(savedPatientInDB);
    }

    @Override
    public List<PatientDTORequest> getAllPatients() {
        //check if patient list is empty or not
        List< Patient > availablePatients = patientRepository.findAll();

        if(availablePatients.isEmpty()){
            throw new ApiException("No patient found in the system...");
        }

//        List< PatientDTORequest > patientsDTOList = availablePatients.stream()
//                .map(patient -> modelMapper.map(patient, PatientDTORequest.class))
//                .collect(Collectors.toList());
//
//            availablePatients.forEach(patient -> {
//                User createdBy = patient.getCreatedBy();
//                if (createdBy != null) {
//                    String createdByUserName = createdBy.getUserName();
//                    patientsDTOList.stream()
//                            .filter(p -> p.getPatientId().equals(patient.getPatientId()))
//                            .findFirst()
//                            .ifPresent(p -> p.setCreatedBy(createdByUserName));
//                }
//            });
        return mapPatientsToDTOs(availablePatients);
    }

    @Override
    public PatientDTORequest searchPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient","patientId",id));

//        PatientDTORequest patientDTO = modelMapper.map(patient, PatientDTORequest.class);
//        User createdBy = patient.getCreatedBy();
//        if (createdBy != null) {
//            patientDTO.setCreatedBy(createdBy.getUserName());
//        }
        return mapPatientToDTO(patient);
    }

    @Override
    public PatientDTORequest searchPatientByPhoneNumber(String phoneNumber) {

        Patient patient = patientRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Patient","phoneNumber",phoneNumber));

//        PatientDTORequest patientDTO = modelMapper.map(patient, PatientDTORequest.class);
//        User createdBy = patient.getCreatedBy();
//        if (createdBy != null) {
//            patientDTO.setCreatedBy(createdBy.getUserName());
//        }
        return mapPatientToDTO(patient);

    }

    @Override
    public List<PatientDTORequest> searchPatientByName(String name) {
        List<Patient> patients = patientRepository.findByNameLikeIgnoreCase('%'+name+'%');

        if(patients.isEmpty()){
            throw new ResourceNotFoundException("Patient","name",name);
        }

//        List<PatientDTORequest> patientDTOList = patients.stream().map(
//                patient -> modelMapper.map(patient, PatientDTORequest.class))
//                .collect(Collectors.toList());
//
//        patients.forEach(
//                patient -> {
//                    User createdBy = patient.getCreatedBy();
//                    if(createdBy!=null){
//                        String createdByUserName = createdBy.getUserName();
//                        patientDTOList.stream().map(
//                                patientToMap ->{
//                                    if(patientToMap.getPatientId().equals(patient.getPatientId())){
//                                        patientToMap.setCreatedBy(createdByUserName);
//                                    }
//                                    return patientToMap;
//                                }
//                        );
//                    }
//                }
//        );

        return mapPatientsToDTOs(patients);
    }

    @Override
    public List<PatientDTORequest> getAllPatientsByLoggedInUser( User user) {

        List<Patient> patients = patientRepository.findByCreatedBy(user);
        if (patients.isEmpty()){
            throw new ApiException("No patient found created by user "+user.getUserName());
        }
//            List<PatientDTORequest> patientDTOList = patients.stream().map(
//                    patient -> modelMapper.map(patient, PatientDTORequest.class))
//                    .collect(Collectors.toList());
//
//        patients.forEach(
//                patient -> {
//                    User createdBy = patient.getCreatedBy();
//                    if(createdBy!=null){
//                        String createdByUserName = createdBy.getUserName();
//                        patientDTOList.stream().map(
//                                patientToMap ->{
//                                    if(patientToMap.getPatientId().equals(patient.getPatientId())){
//                                        patientToMap.setCreatedBy(createdByUserName);
//                                    }
//                                    return patientToMap;
//                                }
//                        );
//                    }
//                }
//        );

        return mapPatientsToDTOs(patients);
    }

    @Override
    public List<PatientDTORequest> searchPatientsByVillageName(String villageName) {
        List<Patient> patients = patientRepository.findByVillageLikeIgnoreCase('%'+villageName+'%');

        if(patients.isEmpty()){
            throw new ResourceNotFoundException("Patient","villageName",villageName);
        }

        return mapPatientsToDTOs(patients);
    }

    @Override
    public List<PatientDTORequest> searchPatientsByDiseaseName(String diseaseName) {
        List<Patient> patients = patientRepository.findByDiseaseLikeIgnoreCase('%'+diseaseName+'%');

        if(patients.isEmpty()){
            throw new ResourceNotFoundException("Patient","diseaseName",diseaseName);
        }

        return mapPatientsToDTOs(patients);
    }

    @Override
    public PatientDTORequest updatePatient(PatientDTORequest patientDTORequest, Long id) {

        Patient existedPatient = patientRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Patient","patientId",id)
        );

        Patient patientToUpdate = modelMapper.map(patientDTORequest, Patient.class);
        //existedPatient.setCreatedBy(patientToUpdate.getCreatedBy());
        existedPatient.setName(patientToUpdate.getName());
        existedPatient.setAge(patientToUpdate.getAge());
        existedPatient.setGender(patientToUpdate.getGender());
        existedPatient.setPhoneNumber(patientToUpdate.getPhoneNumber());
        existedPatient.setVillage(patientToUpdate.getVillage());
        existedPatient.setDisease(patientToUpdate.getDisease());

        Patient updatedPatient = patientRepository.save(existedPatient);

        return mapPatientToDTO(updatedPatient);
    }

    @Override
    public PatientDTORequest deletePatient(Long id) {

        Patient patientToDelete = patientRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Patient","patientId",id)
        );
        patientRepository.delete(patientToDelete);

        return mapPatientToDTO(patientToDelete);
    }
}
