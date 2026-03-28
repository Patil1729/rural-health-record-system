package com.ruralHealth.vaccination.payload;

import com.ruralHealth.entity.VaccinationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccinationResponse {

    private Long patientId;
    private String vaccineName;
    private Integer doseNumber;
    private LocalDate vaccinationDate;
    private LocalDate nextDueDate;
    private VaccinationStatus status;
}
