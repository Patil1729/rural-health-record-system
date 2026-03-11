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

    @GetMapping("/id/{id}")
    public ResponseEntity<PatientDTORequest> getPatientById(@PathVariable Long id){
        PatientDTORequest patientById = patientService.searchPatientById(id);
        return new ResponseEntity<>(patientById,HttpStatus.OK);
    }

    @GetMapping("/phoneNumber/{phoneNumber}")
    public ResponseEntity<PatientDTORequest> getPatientByPhoneNumber(@PathVariable String phoneNumber){
        PatientDTORequest patientByPhoneNumber = patientService.searchPatientByPhoneNumber(phoneNumber);
        return new ResponseEntity<>(patientByPhoneNumber,HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity< List<PatientDTORequest> > getPatientsByName(@PathVariable String name){
        List<PatientDTORequest> patientDTORequestList = patientService.searchPatientByName(name);
        return new ResponseEntity<>(patientDTORequestList,HttpStatus.OK);
     }

     @GetMapping("/createdByLoggedInUser")
     public ResponseEntity < List<PatientDTORequest> > getPatientsByLoggedInUser(){
        User user = authUtil.loggedInUser();
        List<PatientDTORequest> patientDTORequestList = patientService.getAllPatientsByLoggedInUser(user);
        return new ResponseEntity<>(patientDTORequestList,HttpStatus.OK);
     }

     @GetMapping("/villageName/{villageName}")
     public ResponseEntity< List <PatientDTORequest> > getAllPatientsByVillageName( @PathVariable String villageName){
        List<PatientDTORequest> patientDTORequestList = patientService.searchPatientsByVillageName(villageName);
        return new ResponseEntity<>(patientDTORequestList,HttpStatus.OK);
     }

     @GetMapping("/diseaseName/{diseaseName}")
     public ResponseEntity < List< PatientDTORequest >> getAllPatientsByDiseaseName(@PathVariable String diseaseName){
        List<PatientDTORequest> patientDTORequestList = patientService.searchPatientsByDiseaseName(diseaseName);
        return new ResponseEntity<>(patientDTORequestList,HttpStatus.OK);
     }


     @PutMapping("/update/{id}")
     public ResponseEntity< PatientDTORequest > updatePatient(@RequestBody PatientDTORequest patientDTORequest, @PathVariable Long id){

        PatientDTORequest response = patientService.updatePatient(patientDTORequest,id);
        return new ResponseEntity<>(response,HttpStatus.OK);
     }


     @DeleteMapping("/delete/{id}")
     public ResponseEntity < PatientDTORequest > deletePatient(@PathVariable Long id){
        PatientDTORequest response = patientService.deletePatient(id);
        return new ResponseEntity<>(response,HttpStatus.OK);
     }


}
