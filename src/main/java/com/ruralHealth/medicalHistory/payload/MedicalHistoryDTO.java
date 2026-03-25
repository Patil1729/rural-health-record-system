package com.ruralHealth.medicalHistory.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalHistoryDTO {

    private Long patientId;
    private Long doctorId;
    private Long appointmentId;
    private String diagnosis;
    private String symptoms;
    private String prescription;
    private String notes;
    private LocalDate visitDate;



}
