package com.ruralHealth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
@Entity
@Table(name = "medical_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;


    private String diagnosis;

    private String symptoms;

    private String prescription;

    private String notes;

    private LocalDate visitDate;

    @CreatedDate
    private LocalDate createdAt;
}
