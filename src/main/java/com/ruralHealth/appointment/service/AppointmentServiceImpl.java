package com.ruralHealth.appointment.service;

import com.ruralHealth.appointment.payload.AppointmentDTO;
import com.ruralHealth.repository.AppointmentRepository;
import com.ruralHealth.entity.Appointment;
import com.ruralHealth.entity.AppointmentStatus;
import com.ruralHealth.entity.Doctor;
import com.ruralHealth.entity.Patient;
import com.ruralHealth.exception.ApiException;
import com.ruralHealth.exception.ResourceNotFoundException;
import com.ruralHealth.repository.DoctorRepository;
import com.ruralHealth.repository.PatientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private static final int MAX_APPOINTMENTS_PER_DAY = 20;

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ModelMapper modelMapper;


    public AppointmentDTO mapToDTO(Appointment appointment) {
        return modelMapper.map(appointment, AppointmentDTO.class);
    }

    public List<AppointmentDTO> mapToDTOList(List<Appointment> appointments) {
        return appointments.stream().map(this::mapToDTO).toList();
    }

    @Override
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {

        Patient patient = patientRepository.findById(appointmentDTO.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", appointmentDTO.getPatientId()));
        Doctor doctor = doctorRepository.findById(appointmentDTO.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", appointmentDTO.getDoctorId()));

        if(appointmentRepository.existsByDoctorAndAppointmentDateAndStartTime
                (doctor,appointmentDTO.getAppointmentDate(),appointmentDTO.getStartTime())){
            throw new ApiException("Doctor is not available on this time slot");
        }

        if(appointmentRepository.existsByDoctorAndPatientAndAppointmentDate(doctor, patient, appointmentDTO.getAppointmentDate())){
            throw new ApiException("Patient already has appointment with this doctor today");
        }

        long appointmentCount = appointmentRepository.countByDoctorAndAppointmentDate(
                doctor,
                appointmentDTO.getAppointmentDate());

        if (appointmentCount >= MAX_APPOINTMENTS_PER_DAY) {
            throw new ApiException("Doctor has reached maximum appointments for the day");
        }

         Appointment appointment = modelMapper.map(appointmentDTO, Appointment.class);
         appointment.setPatient(patient);
         appointment.setDoctor(doctor);
         appointment.setStatus(AppointmentStatus.BOOKED);

         Appointment savedAppointment = appointmentRepository.save(appointment);

         return mapToDTO(savedAppointment);

    }

    @Override
    public List<AppointmentDTO> getAllAppointments() {

        List<Appointment > appointments = appointmentRepository.findAll();
        if (appointments.isEmpty()) {
            throw new ApiException("No appointments found in the system");
        }

        return mapToDTOList(appointments);
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByDoctor(Long doctorId) {

        List<Appointment> appointmentList = appointmentRepository.findByDoctorDoctorId(doctorId);
            if (appointmentList.isEmpty()) {
                throw new ResourceNotFoundException("Appointment", "doctorId", doctorId);
            }

        return mapToDTOList(appointmentList);
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByPatient(Long patientId) {
        List<Appointment> appointmentList = appointmentRepository.findByPatientPatientId(patientId);
        if (appointmentList.isEmpty()) {
            throw new ResourceNotFoundException("Appointment", "patient", patientId);
        }

        return mapToDTOList(appointmentList);
    }

    @Override
    public AppointmentDTO updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        if (status == null) {
            throw new ApiException("Appointment status cannot be null");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

        AppointmentStatus currentStatus = appointment.getStatus();

        if (!isValidTransition(currentStatus, status)) {
            throw new ApiException("Invalid appointment status transition from "
                    + currentStatus + " to " + status);
        }

        appointment.setStatus(status);

        return mapToDTO(appointmentRepository.save(appointment));
    }

    @Override
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new ApiException("Completed appointment cannot be cancelled");
        }

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new ApiException("Appointment already cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }


    private boolean isValidTransition(AppointmentStatus current, AppointmentStatus next) {

        return switch (current) {
            case BOOKED -> next == AppointmentStatus.CONFIRMED || next == AppointmentStatus.CANCELLED;
            case CONFIRMED -> next == AppointmentStatus.COMPLETED || next == AppointmentStatus.CANCELLED;
            case COMPLETED, CANCELLED -> false;
        };
    }
}
