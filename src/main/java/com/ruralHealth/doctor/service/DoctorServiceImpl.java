package com.ruralHealth.doctor.service;

import com.ruralHealth.doctor.payload.DoctorDTO;
import com.ruralHealth.entity.Doctor;
import com.ruralHealth.entity.Specialization;
import com.ruralHealth.exception.ApiException;
import com.ruralHealth.exception.ResourceNotFoundException;
import com.ruralHealth.repository.DoctorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    public DoctorRepository doctorRepository;
    @Autowired
    public ModelMapper modelMapper;

    private DoctorDTO mapToDTO(Doctor doctor){
        return modelMapper.map(doctor, DoctorDTO.class);
    }

    private List<DoctorDTO> mapToDTOList(List<Doctor> doctors){
        return doctors.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public DoctorDTO createDoctor(DoctorDTO doctor) {
        Doctor mapDoctor = modelMapper.map(doctor, Doctor.class);

        doctorRepository.findByLicenseNumber(mapDoctor.getLicenseNumber())
                .ifPresent(doc->{
                    throw new ApiException("Doctor with license number "+mapDoctor.getLicenseNumber()+" already exists");
                });
        // Specialization specialization = Specialization.valueOf(doctor.getSpecialization().toUpperCase());
       // mapDoctor.setSpecialization(specialization);
        Doctor savedDoctor = doctorRepository.save(mapDoctor);
        return mapToDTO(savedDoctor);
    }

    @Override
    public DoctorDTO getDoctorById(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor","doctorId",doctorId));
        return mapToDTO(doctor);
    }

    @Override
    public List<DoctorDTO> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
       if(doctors.isEmpty()){
           throw new ApiException("No doctors found in the system");
       }
        return mapToDTOList(doctors);
    }

    @Override
    public List<DoctorDTO> getDoctorsBySpecialization(String specialization) {

        List< Doctor > doctorList = doctorRepository
                .findBySpecialization(Specialization.valueOf(specialization.toUpperCase()));
        if (doctorList.isEmpty()){
            throw new ResourceNotFoundException("Doctor","specialization",specialization);
        }

        return mapToDTOList(doctorList);
    }

    @Override
    public DoctorDTO updateDoctor(Long doctorId, DoctorDTO doctorDTO) {

       Doctor existedDoctor = doctorRepository.findById(doctorId)
               .orElseThrow(() -> new ResourceNotFoundException("Doctor","doctorId",doctorId));

       modelMapper.map(doctorDTO, existedDoctor);
       Doctor updatedDoctor = doctorRepository.save(existedDoctor);
       return mapToDTO(updatedDoctor);

    }

    @Override
    public DoctorDTO deleteDoctor(Long doctorId) {
        Doctor doctorToDelete = doctorRepository.findById(doctorId).orElseThrow(
                ()-> new ResourceNotFoundException("Doctor","doctorId",doctorId)
        );
        doctorRepository.delete(doctorToDelete);

        return mapToDTO(doctorToDelete);
    }
}
