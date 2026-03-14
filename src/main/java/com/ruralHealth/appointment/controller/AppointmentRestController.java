package com.ruralHealth.appointment.controller;

import com.ruralHealth.appointment.payload.AppointmentDTO;
import com.ruralHealth.appointment.service.AppointmentService;
import com.ruralHealth.entity.AppointmentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentRestController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/create")
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO dto){

        return new ResponseEntity<>(
                appointmentService.createAppointment(dto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/allappointments")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments(){
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDoctor(@PathVariable Long doctorId){
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPatient(@PathVariable Long patientId){
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }

    @PatchMapping("/update/{appointmentId}/status")
    public ResponseEntity<AppointmentDTO> updateStatus(
            @PathVariable Long appointmentId,
            @RequestParam AppointmentStatus status){

        return ResponseEntity.ok(
                appointmentService.updateAppointmentStatus(appointmentId, status)
        );
    }

    @DeleteMapping("/delete/{appointmentId}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long appointmentId){

        appointmentService.cancelAppointment(appointmentId);

        return ResponseEntity.ok("Appointment cancelled");
    }
}
