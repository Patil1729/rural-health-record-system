package com.ruralHealth.dashBoard.service;

import com.ruralHealth.appointment.service.AppointmentServiceImpl;
import com.ruralHealth.dashBoard.payload.DashboardDto;
import com.ruralHealth.dashBoard.payload.DashboardSummaryDTO;
import com.ruralHealth.entity.Appointment;
import com.ruralHealth.entity.MedicalHistory;
import com.ruralHealth.entity.Patient;
import com.ruralHealth.entity.Vaccination;
import com.ruralHealth.exception.ResourceNotFoundException;
import com.ruralHealth.medicalHistory.service.MedicalHistoryServiceImpl;
import com.ruralHealth.patient.payload.PatientDTORequest;
import com.ruralHealth.repository.*;
import com.ruralHealth.vaccination.service.VaccinationServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;
    @Autowired
    private VaccinationRepository vaccinationRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AppointmentServiceImpl appointmentService;
    @Autowired
    private VaccinationServiceImpl vaccinationService;
    @Autowired
    private MedicalHistoryServiceImpl medicalHistoryService;


    @Override
    public DashboardDto getDashboard(Long patientId) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));

        LocalDate today = LocalDate.now();

        List<Appointment> upcoming = appointmentRepository
                .findByPatient_PatientIdAndAppointmentDateAfter(patientId, today);

        List<Appointment> past = appointmentRepository
                .findByPatient_PatientIdAndAppointmentDateBefore(patientId, today);

        List<Vaccination> vaccinations = vaccinationRepository
                .findByPatient_PatientId(patientId);

        List<Vaccination> dueVaccines = vaccinationRepository
                .findByPatient_PatientIdAndNextDueDateBefore(patientId, today.plusDays(1));

        List<MedicalHistory> history = medicalHistoryRepository
                .findByPatient_PatientId(patientId);

        DashboardDto response = new DashboardDto();

        response.setPatient(modelMapper.map(patient, PatientDTORequest.class));
        response.setUpcomingAppointments(appointmentService.mapToDTOList(upcoming));
        response.setPastAppointments(appointmentService.mapToDTOList(past));
        response.setVaccinations(vaccinationService.mapToDTOList(vaccinations));

        response.setDueVaccinations(vaccinationService.mapToDTOList(dueVaccines));
        response.setMedicalHistory(medicalHistoryService.mapToDTOList(history));

        response.setTotalVisits(past.size());
        response.setCompletedVaccines(vaccinations.size());

        return response;
    }


    public DashboardSummaryDTO getDashboardSummary() {

        long totalPatients = patientRepository.count();
        long totalDoctors = doctorRepository.count();
        long totalAppointments = appointmentRepository.count();

        return new DashboardSummaryDTO(
                totalPatients,
                totalDoctors,
                totalAppointments
        );
    }

}