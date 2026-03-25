package com.ruralHealth.medicalHistory.service;

import com.ruralHealth.entity.Appointment;
import com.ruralHealth.entity.Doctor;
import com.ruralHealth.entity.MedicalHistory;
import com.ruralHealth.entity.Patient;
import com.ruralHealth.exception.ResourceNotFoundException;
import com.ruralHealth.medicalHistory.payload.MedicalHistoryDTO;
import com.ruralHealth.repository.AppointmentRepository;
import com.ruralHealth.repository.DoctorRepository;
import com.ruralHealth.repository.MedicalHistoryRepository;
import com.ruralHealth.repository.PatientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class MedicalHistoryIMPL implements MedicalHistoryService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public MedicalHistoryDTO createMedicalHistory(MedicalHistoryDTO medicalHistoryDTO) {
        MedicalHistory medicalHistory = modelMapper.map(medicalHistoryDTO, MedicalHistory.class);

        Patient patient = patientRepository.findById(medicalHistoryDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + medicalHistoryDTO.getPatientId()));
        medicalHistory.setPatient(patient);

        Doctor doctor = doctorRepository.findById(medicalHistoryDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + medicalHistoryDTO.getDoctorId()));
        medicalHistory.setDoctor(doctor);

        Appointment appointment = appointmentRepository.findById(medicalHistoryDTO.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + medicalHistoryDTO.getAppointmentId()));
        medicalHistory.setAppointment(appointment);

        MedicalHistory savedMedicalHistory = medicalHistoryRepository.save(medicalHistory);
        return modelMapper.map(savedMedicalHistory, MedicalHistoryDTO.class);

    }

    @Override
    public MedicalHistoryDTO getMedicalHistoryById(Long id) {

        MedicalHistory medicalHistory = medicalHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical history not found with id: " + id));
        return modelMapper.map(medicalHistory, MedicalHistoryDTO.class);

    }

    @Override
    public List<MedicalHistoryDTO> getMedicalHistoryByPatientId(Long patientId) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        List<MedicalHistory> medicalHistoryList = medicalHistoryRepository.findByPatient(patient);

     List<MedicalHistoryDTO> dtoList=   medicalHistoryList.stream().map(medicalHistory -> {
                    MedicalHistoryDTO dto = modelMapper.map(medicalHistory, MedicalHistoryDTO.class);
                    dto.setPatientId(medicalHistory.getPatient().getPatientId());
                    dto.setDoctorId(medicalHistory.getDoctor().getDoctorId());
                    dto.setAppointmentId(medicalHistory.getAppointment().getAppointmentId());
                    return dto;
                }
        ).collect(toList());
        return dtoList;
    }

    @Override
    public List<MedicalHistoryDTO> getMedicalHistoryByDoctorId(Long doctorId) {

        //another way to reduce Db call

        List<MedicalHistory> medicalHistoryList = medicalHistoryRepository.findByDoctor_doctorId(doctorId);

        if(medicalHistoryList.isEmpty()){
            throw new ResourceNotFoundException("MedicalHistory", "DoctorId " , doctorId);
        }

        return medicalHistoryList.stream().map(
                d-> {
                    MedicalHistoryDTO dto = modelMapper.map(d, MedicalHistoryDTO.class);
                    if (d.getDoctor() != null) {
                        dto.setDoctorId(d.getDoctor().getDoctorId());
                    }
                    if (d.getPatient() != null) {
                        dto.setPatientId(d.getPatient().getPatientId());
                    }
                    if (d.getAppointment() != null) {
                        dto.setAppointmentId(d.getAppointment().getAppointmentId());
                    }

                    return dto;
                }).toList();
    }

}
