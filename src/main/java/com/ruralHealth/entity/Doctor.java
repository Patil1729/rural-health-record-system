package com.ruralHealth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "doctors", uniqueConstraints = {
        @UniqueConstraint(columnNames = "phoneNumber"),
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "licenseNumber")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    private String name;

    @Email
    private String email;

    @NotBlank
    private String phoneNumber;
    @NotBlank
    @Column(unique = true)
    private String licenseNumber;

    private String qualification;
    private Integer experienceYears;
    private String hospitalName;
    private String village;

    @Enumerated(EnumType.STRING)
    private Specialization specialization;


}
