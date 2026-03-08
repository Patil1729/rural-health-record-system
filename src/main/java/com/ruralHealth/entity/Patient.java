package com.ruralHealth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "patient", uniqueConstraints = {
        @UniqueConstraint(columnNames = "phone"),
        @UniqueConstraint(columnNames = "email")
})
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patientId")
    private Long patientId;

    private String name;
    private Integer age;
    private String gender;

    @NotBlank
    private String phone;

    @Email
    private String email;

    private String village;
    private String bloodGroup;
    private String disease;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    private LocalDate registrationDate;



}