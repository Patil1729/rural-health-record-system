package com.ruralHealth.repository;

import com.ruralHealth.entity.MedicalHistory;
import com.ruralHealth.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {

    List<MedicalHistory> findByPatient(Patient patient);

    List<MedicalHistory> findByDoctor_doctorId(Long doctorId);
}
