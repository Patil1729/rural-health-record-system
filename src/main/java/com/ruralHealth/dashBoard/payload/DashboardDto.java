package com.ruralHealth.dashBoard.payload;

import com.ruralHealth.appointment.payload.AppointmentDTO;
import com.ruralHealth.medicalHistory.payload.MedicalHistoryDTO;
import com.ruralHealth.patient.payload.PatientDTORequest;
import com.ruralHealth.vaccination.payload.VaccinationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDto {

    private PatientDTORequest patient;

    private List<AppointmentDTO> upcomingAppointments;

    private List<AppointmentDTO> pastAppointments;

    private List<VaccinationDTO> vaccinations;

    private List<VaccinationDTO> dueVaccinations;

    private List<MedicalHistoryDTO> medicalHistory;

    private int totalVisits;

    private int completedVaccines;

}
