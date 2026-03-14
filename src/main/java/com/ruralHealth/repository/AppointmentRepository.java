package com.ruralHealth.repository;

import com.ruralHealth.entity.Appointment;
import com.ruralHealth.entity.Doctor;
import com.ruralHealth.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {


    boolean existsByDoctorAndAppointmentDate(Doctor doctor, LocalDate appointmentDate);

    boolean existsByDoctorAndAppointmentDateAndStartTime(
            Doctor doctor,
            LocalDate date,
            LocalTime startTime
    );

    boolean existsByDoctorAndPatientAndAppointmentDate(
            Doctor doctor,
            Patient patient,
            LocalDate date
    );

    long countByDoctorAndAppointmentDate(Doctor doctor, LocalDate appointmentDate);

    List<Appointment> findByDoctorDoctorId(Long doctorId);

    List<Appointment> findByPatientPatientId(Long patientId);
}
