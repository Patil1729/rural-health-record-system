package com.ruralHealth.medicalHistory.controller;

import com.ruralHealth.medicalHistory.payload.MedicalHistoryDTO;
import com.ruralHealth.medicalHistory.service.MedicalHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-history")
public class MedicalHistoryRestController {

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @PostMapping
    public ResponseEntity<MedicalHistoryDTO> create(@RequestBody MedicalHistoryDTO dto) {
        return ResponseEntity.ok(medicalHistoryService.createMedicalHistory(dto));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalHistoryDTO>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalHistoryService.getMedicalHistoryByPatientId(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<MedicalHistoryDTO>> getByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(medicalHistoryService.getMedicalHistoryByDoctorId(doctorId));
    }

    @GetMapping("/id/{historyId}")
    public ResponseEntity<MedicalHistoryDTO> getByMedicalHistory(@PathVariable Long historyId) {
        return ResponseEntity.ok(medicalHistoryService.getMedicalHistoryById(historyId));
    }

}
