package com.ruralHealth.doctor.controller;

import com.ruralHealth.doctor.payload.DoctorDTO;
import com.ruralHealth.doctor.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/doctor/")
public class DoctorRestController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/create") //only admin will create doctor profile
    public ResponseEntity<DoctorDTO> createDoctor(@RequestBody DoctorDTO doctor){
        return ResponseEntity.ok(doctorService.createDoctor(doctor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctor(@PathVariable Long id){
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @GetMapping("/alldoctors")
    public ResponseEntity<List<DoctorDTO>> getAllDoctors(){
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/specialization/{type}")
    public ResponseEntity<List<DoctorDTO>> getDoctorsBySpecialization(@PathVariable String type){
        return ResponseEntity.ok(doctorService.getDoctorsBySpecialization(type));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Long id,
                                               @RequestBody DoctorDTO doctor){
        return ResponseEntity.ok(doctorService.updateDoctor(id, doctor));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DoctorDTO> deleteDoctor(@PathVariable Long id){
        return ResponseEntity.ok(doctorService.deleteDoctor(id));
    }


}
