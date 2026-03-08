package com.ruralHealth.patient.controller;

import com.ruralHealth.entity.User;
import com.ruralHealth.patient.payload.PatientDTORequest;
import com.ruralHealth.patient.service.PatientService;
import com.ruralHealth.security.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patients")
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


}
