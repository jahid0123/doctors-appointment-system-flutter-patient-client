package com.jmjbrothers.doctorsappointmentsystem.service;

import com.jmjbrothers.doctorsappointmentsystem.dto.PatientRegisterRequestDto;
import com.jmjbrothers.doctorsappointmentsystem.model.Patient;
import com.jmjbrothers.doctorsappointmentsystem.model.User;
import com.jmjbrothers.doctorsappointmentsystem.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;


    public PatientService(PatientRepository patientRepository, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Patient registerNewPatient(PatientRegisterRequestDto request) {

        Patient existPatient = patientRepository.findByEmail(request.getEmail()).orElse(null);
        if (existPatient != null){
            throw new RuntimeException("Patient already exist by email: "+request.getEmail());
        }

        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setEmail(request.getEmail());
        patient.setPassword(passwordEncoder.encode(request.getPassword()));
        patient.setDob(request.getDob());
        patient.setGender(request.getGender());

        Patient savePatient =  patientRepository.save(patient);

        if (savePatient != null){
            return savePatient;
        }else {
            return null;
        }
    }

    //Get all patients by admin
    @Transactional
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }


    public Patient myProfileInfo(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Patient not found")
        );
        return patient;
    }
}
