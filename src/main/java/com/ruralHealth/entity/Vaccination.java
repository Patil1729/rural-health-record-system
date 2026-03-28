package com.ruralHealth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Entity
@Table(name = "vaccination")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vaccination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  vaccinationId;
    private String vaccineName;
    private Integer doseNumber;
    private LocalDate vaccinationDate;
    private LocalDate nextDueDate;
    private String administeredBy;
    private String notes;

    @Enumerated(EnumType.STRING)
    private VaccinationStatus  status;

    //many patient have one vaccine
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @CreatedDate
    private LocalDate createdAt;

}
