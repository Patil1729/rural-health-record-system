package com.ruralHealth.doctor.payload;

import com.ruralHealth.entity.Specialization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDTO {

    private Long doctorId;

    private String name;
    private String email;
    private String phoneNumber;
    private String licenseNumber;
    private String qualification;
    private Integer experienceYears;
    private String hospitalName;
    private String village;
    private Specialization specialization;

}
