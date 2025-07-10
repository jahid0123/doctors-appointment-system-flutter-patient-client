package com.jmjbrothers.doctorsappointmentsystem.controller;

import com.jmjbrothers.doctorsappointmentsystem.dto.AppointmentDto;
import com.jmjbrothers.doctorsappointmentsystem.dto.AppointmentResponseDto;
import com.jmjbrothers.doctorsappointmentsystem.dto.DoctorListDto;
import com.jmjbrothers.doctorsappointmentsystem.dto.PrescriptionResponseDto;
import com.jmjbrothers.doctorsappointmentsystem.model.Appointment;
import com.jmjbrothers.doctorsappointmentsystem.model.Patient;
import com.jmjbrothers.doctorsappointmentsystem.model.Prescription;
import com.jmjbrothers.doctorsappointmentsystem.service.AppointmentService;
import com.jmjbrothers.doctorsappointmentsystem.service.DoctorService;
import com.jmjbrothers.doctorsappointmentsystem.service.PatientService;
import com.jmjbrothers.doctorsappointmentsystem.service.PrescriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    private final DoctorService doctorService;
    private final PrescriptionService prescriptionService;
    private final AppointmentService appointmentService;
    private final PatientService patientService;

    public PatientController(DoctorService doctorService, PrescriptionService prescriptionService, AppointmentService appointmentService, PatientService patientService) {
        this.doctorService = doctorService;
        this.prescriptionService = prescriptionService;
        this.appointmentService = appointmentService;
        this.patientService = patientService;
    }

    //Get All Doctors By the Patient.
    @GetMapping("/get/doctors")
    public ResponseEntity<?> getAllDoctors(){
        List<DoctorListDto> allDoctorList = doctorService.getAllDoctorsByPatient();
        return new ResponseEntity<>(allDoctorList, HttpStatus.OK);
    }

    //Get All Prescription By the Patient.
    @GetMapping("/get/all/prescriptions/by")
    public ResponseEntity<?> getPrescriptionByDoctorId(@RequestParam("id") Long id) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionByPatientId(id);
        return new ResponseEntity<>(prescriptions, HttpStatus.OK);
    }

    @GetMapping("/get/prescription/by/app")
    public ResponseEntity<?> getPrescriptionByAppId(@RequestParam Long id) {
        PrescriptionResponseDto prescriptionByAppId = prescriptionService.getPrescriptionByAppId(id);
        return new ResponseEntity<>(prescriptionByAppId, HttpStatus.OK);
    }

    //Book Appointment By the Patient.
    @PostMapping("/book/appointment/by")
    public ResponseEntity<?> bookAppointmentByPatient(@RequestBody AppointmentDto appointmentDto) {
        Appointment appointment = appointmentService.bookAppointmentByPatient(appointmentDto);
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    //Get All appointment By the Patient.
    @GetMapping("/all/appointmentlist")
    public ResponseEntity<?> getAllAppointmentByPatientId(@RequestParam("id") Long id) {
        List<AppointmentResponseDto> prescriptions = appointmentService.getAllAppointmentByPatientId(id);
        return new ResponseEntity<>(prescriptions, HttpStatus.OK);
    }

    //Get All appointment By the Patient.
    @GetMapping("/me")
    public ResponseEntity<?> myProfileInfo(@RequestParam("id") Long id) {
        Patient patient = patientService.myProfileInfo(id);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

}
