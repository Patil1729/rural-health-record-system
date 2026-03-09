package com.ruralHealth.patient.controller;

import com.ruralHealth.entity.User;
import com.ruralHealth.patient.payload.PatientDTORequest;
import com.ruralHealth.patient.service.PatientService;
import com.ruralHealth.security.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
public class PatientRestController {

    @Autowired
    PatientService patientService;

    @Autowired
    private AuthUtils authUtil;

    @PostMapping("/create")
    public ResponseEntity<PatientDTORequest> createPatient(@RequestBody PatientDTORequest patientDTORequest) {
        User user = authUtil.loggedInUser();
        PatientDTORequest savedPatientDTO = patientService.savePatient(patientDTORequest, user);
        return new ResponseEntity<>(savedPatientDTO, HttpStatus.CREATED);
    }

    @GetMapping("/allPatients")
    public ResponseEntity< List<PatientDTORequest> > getAllPatients(){
        User user = authUtil.loggedInUser();
        List<PatientDTORequest> patientDTORequestList = patientService.getAllPatients();
        return new ResponseEntity<>(patientDTORequestList,HttpStatus.OK);
    }

}
