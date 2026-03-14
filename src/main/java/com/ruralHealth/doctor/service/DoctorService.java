package com.ruralHealth.doctor.service;

import com.ruralHealth.doctor.payload.DoctorDTO;

import java.util.List;

public interface DoctorService {

    DoctorDTO createDoctor(DoctorDTO doctor);

    DoctorDTO getDoctorById(Long doctorId);

    List<DoctorDTO> getAllDoctors();

    List<DoctorDTO> getDoctorsBySpecialization(String specialization);

    DoctorDTO updateDoctor(Long doctorId, DoctorDTO doctor);

    DoctorDTO deleteDoctor(Long doctorId);


}
