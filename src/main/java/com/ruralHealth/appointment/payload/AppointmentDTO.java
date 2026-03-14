package com.ruralHealth.appointment.payload;

import com.ruralHealth.entity.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {

    private Long appointmentId;

    private Long patientId;

    private Long doctorId;

    private LocalDate appointmentDate;

    private String symptoms;

    private String village;

    private AppointmentStatus status;

    private LocalTime startTime;
}
