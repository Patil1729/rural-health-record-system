package com.ruralHealth.vaccination.controller;

import com.ruralHealth.vaccination.payload.VaccinationDTO;
import com.ruralHealth.vaccination.payload.VaccinationResponse;
import com.ruralHealth.vaccination.service.VaccinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vaccination")
public class VaccinationRestController {
    @Autowired
    private VaccinationService vaccinationService;

    @PostMapping("/create")
    public ResponseEntity< VaccinationResponse > createVaccine(@RequestBody VaccinationDTO vaccinationDTO){
        return new ResponseEntity<>(vaccinationService.createVaccine(vaccinationDTO), HttpStatus.CREATED);
    }


    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<VaccinationResponse> > getRecordByPatientId(@PathVariable Long patientId){
        List<VaccinationResponse> responseList = vaccinationService.getRecordByPatient(patientId);
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }


        // Manual trigger
    @PostMapping("/reminder")
    public ResponseEntity<String> triggerReminder() {
        vaccinationService.sendVaccinationReminders();
        return ResponseEntity.ok("Reminders triggered successfully");
    }


    @GetMapping("/due")
    public ResponseEntity<List<VaccinationDTO>> getDue(@RequestParam String date) {
        LocalDate dueDate = LocalDate.parse(date);
        return ResponseEntity.ok(vaccinationService.getDueVaccinations(dueDate));
    }

    @PatchMapping("/{id}/next-dose")
    public ResponseEntity<VaccinationDTO> updateNextDose(@PathVariable Long id, @RequestParam String newDate) {
        LocalDate date = LocalDate.parse(newDate);
        return ResponseEntity.ok(vaccinationService.updateNextDose(id, date)
        );
    }


    @Scheduled(cron = "0 0 9 * * *")
    public void autoReminder() {
        vaccinationService.sendVaccinationReminders();
    }

}



