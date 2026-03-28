package com.ruralHealth.vaccination.payload;

import com.ruralHealth.entity.VaccinationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationDTO {

    private Long patientId;
    private String vaccineName;
    private Integer doseNumber;
    private LocalDate vaccinationDate;
    private String administeredBy;
    private String notes;
    private VaccinationStatus status;

}
