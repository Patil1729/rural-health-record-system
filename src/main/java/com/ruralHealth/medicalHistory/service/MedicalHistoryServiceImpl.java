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
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class MedicalHistoryServiceImpl implements MedicalHistoryService {
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

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.typeMap(MedicalHistoryDTO.class, MedicalHistory.class)
                .addMappings(mapper -> mapper.skip(MedicalHistory::setHistoryId));

        MedicalHistory medicalHistory = modelMapper.map(medicalHistoryDTO, MedicalHistory.class);

        Patient patient = patientRepository.findById(medicalHistoryDTO.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", medicalHistoryDTO.getPatientId()));
        medicalHistory.setPatient(patient);

        Doctor doctor = doctorRepository.findById(medicalHistoryDTO.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", medicalHistoryDTO.getDoctorId()));
        medicalHistory.setDoctor(doctor);

        Appointment appointment = appointmentRepository.findById(medicalHistoryDTO.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", medicalHistoryDTO.getAppointmentId()));
        medicalHistory.setAppointment(appointment);

        MedicalHistory savedMedicalHistory = medicalHistoryRepository.save(medicalHistory);
        return modelMapper.map(savedMedicalHistory, MedicalHistoryDTO.class);

    }

    @Override
    public MedicalHistoryDTO getMedicalHistoryById(Long id) {

        MedicalHistory medicalHistory = medicalHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalHistory", "id", id));
        return modelMapper.map(medicalHistory, MedicalHistoryDTO.class);

    }

    @Override
    public List<MedicalHistoryDTO> getMedicalHistoryByPatientId(Long patientId) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));

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
