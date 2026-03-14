package com.ruralHealth.appointment.service;

import com.ruralHealth.appointment.payload.AppointmentDTO;
import com.ruralHealth.entity.AppointmentStatus;

import java.util.List;

public interface AppointmentService {

    AppointmentDTO createAppointment(AppointmentDTO appointmentDTO);

    List<AppointmentDTO> getAllAppointments();

    List<AppointmentDTO> getAppointmentsByDoctor(Long doctorId);

    List<AppointmentDTO> getAppointmentsByPatient(Long patientId);

    AppointmentDTO updateAppointmentStatus(Long appointmentId, AppointmentStatus status);

    void cancelAppointment(Long appointmentId);


}
