package com.ruralHealth.repository;

import com.ruralHealth.entity.Doctor;
import com.ruralHealth.entity.Specialization;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

   Optional< Doctor> findByName(String name);

   Optional<Doctor> findByLicenseNumber(@NotBlank String licenseNumber);

   List<Doctor> findBySpecialization(Specialization specialization);
}
