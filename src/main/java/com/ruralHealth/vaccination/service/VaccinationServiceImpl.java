package com.ruralHealth.vaccination.service;

import com.ruralHealth.entity.Patient;
import com.ruralHealth.entity.Vaccination;
import com.ruralHealth.entity.VaccinationStatus;
import com.ruralHealth.exception.ApiException;
import com.ruralHealth.exception.ResourceNotFoundException;
import com.ruralHealth.repository.PatientRepository;
import com.ruralHealth.repository.VaccinationRepository;
import com.ruralHealth.service.NotificationService;
import com.ruralHealth.vaccination.payload.VaccinationDTO;
import com.ruralHealth.vaccination.payload.VaccinationResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VaccinationServiceImpl implements VaccinationService {

    private static final Logger log = LoggerFactory.getLogger(VaccinationServiceImpl.class);
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private VaccinationRepository vaccinationRepository;

    @Override
    public VaccinationResponse createVaccine(VaccinationDTO vaccinationDTO) {

        Patient patient = patientRepository.findById(vaccinationDTO.getPatientId())
                .orElseThrow(()-> new ResourceNotFoundException("Patient", "id", vaccinationDTO.getPatientId()));

        if(vaccinationRepository.existsByPatientAndVaccineNameAndDoseNumber(patient,vaccinationDTO.getVaccineName(),vaccinationDTO.getDoseNumber())){
            throw new ApiException("Vaccine already exists for this patient with the same name and dose number");
        }
        Vaccination vaccination = modelMapper.map(vaccinationDTO, Vaccination.class);
        vaccination.setPatient(patient);
        vaccination.setStatus(VaccinationStatus.COMPLETED);

        if(vaccinationDTO.getDoseNumber()==1){
            vaccination.setNextDueDate(vaccinationDTO.getVaccinationDate().plusDays(30));
        }else{
            throw new ApiException("First Dose must be taken before "+vaccinationDTO.getDoseNumber());
        }

        Vaccination savedVaccination = vaccinationRepository.save(vaccination);
        return  modelMapper.map(savedVaccination, VaccinationResponse.class);
    }

    @Override
    public List<VaccinationResponse> getRecordByPatient(Long patientId) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(()-> new ResourceNotFoundException("Patient", "id", patientId));

       List< Vaccination> vaccineList = vaccinationRepository.findByPatient_PatientId(patientId);

       if(vaccineList.isEmpty()){
           throw new ResourceNotFoundException("Patient", "id", patientId);
       }

       return vaccineList.stream()
               .map(vaccination -> modelMapper.map(vaccination, VaccinationResponse.class))
               .toList();


    }


    @Override
    public void sendVaccinationReminders() {

        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<Vaccination> dueList = vaccinationRepository.findByNextDueDate(tomorrow);

        if (dueList.isEmpty()) {
            log.info("No vaccination reminders for tomorrow");
            return;
        }

        dueList.forEach(v -> {
            String patientName = v.getPatient().getName();
            String phone = v.getPatient().getPhoneNumber();

            String message = "Reminder: Your vaccine " + v.getVaccineName()
                    + " is due on " + v.getNextDueDate();

            log.info("Reminder: {} has vaccine {} tomorrow",
                    patientName, v.getVaccineName());

            notificationService.sendReminder(phone,message); // async
        });
    }


    @Override
    public List<VaccinationDTO> getDueVaccinations(LocalDate date) {

        List<Vaccination> list = vaccinationRepository.findByNextDueDate(date);

        return list.stream()
                .map(v -> modelMapper.map(v, VaccinationDTO.class))
                .toList();
    }


    @Override
    public VaccinationDTO updateNextDose(Long vaccinationId, LocalDate newDate) {

        Vaccination vaccination = vaccinationRepository.findById(vaccinationId)
                .orElseThrow(() -> new ResourceNotFoundException("Vaccination", "id", vaccinationId));

        if (vaccination.getDoseNumber() == 3) {
            throw new ApiException("No next dose required for booster");
        }

        vaccination.setNextDueDate(newDate);

        Vaccination updated = vaccinationRepository.save(vaccination);

        return modelMapper.map(updated, VaccinationDTO.class);
    }


    public VaccinationDTO mapToDTO(Vaccination vaccination) {
        return modelMapper.map(vaccination, VaccinationDTO.class);
    }

    public List<VaccinationDTO> mapToDTOList(List<Vaccination> vaccinations) {
        return vaccinations.stream().map(this::mapToDTO).toList();
    }

}
