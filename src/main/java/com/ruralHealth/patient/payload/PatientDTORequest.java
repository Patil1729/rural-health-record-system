package com.ruralHealth.patient.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTORequest {

    private Long patientId;
    private String name;
    private Integer age;
    private String gender;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @Email
    private String email;

    private String village;
    private String bloodGroup;
    private String disease;
    private String createdBy;

    private LocalDate registrationDate;
}
