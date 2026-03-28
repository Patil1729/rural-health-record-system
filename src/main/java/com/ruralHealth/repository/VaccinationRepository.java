package com.ruralHealth.repository;

import com.ruralHealth.entity.Patient;
import com.ruralHealth.entity.Vaccination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccinationRepository  extends JpaRepository<Vaccination,Long> {


    boolean existsByPatientAndVaccineNameAndDoseNumber(Patient patient, String vaccineName, Integer doseNumber);

    List<Vaccination> findByPatient_PatientId(Long patientId);

    List<Vaccination> findByNextDueDate(LocalDate tomorrow);
}
