package com.ruralHealth.repository;

import com.ruralHealth.entity.Patient;
import com.ruralHealth.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {


    Patient findByName(String name);

    Optional< Patient> findByPhoneNumber(@NotBlank String phoneNumber);

    List<Patient> findByNameLikeIgnoreCase(String name);

    List<Patient> findByCreatedBy(User user);

    List<Patient> findByVillageLikeIgnoreCase(String villageName);

    List<Patient> findByDiseaseLikeIgnoreCase(String diseaseName);
}
