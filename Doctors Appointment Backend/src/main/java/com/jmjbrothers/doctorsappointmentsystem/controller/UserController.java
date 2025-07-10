package com.jmjbrothers.doctorsappointmentsystem.controller;

import com.jmjbrothers.doctorsappointmentsystem.dto.AppointmentResponseDto;
import com.jmjbrothers.doctorsappointmentsystem.dto.DoctorDto;
import com.jmjbrothers.doctorsappointmentsystem.dto.DoctorRegisterRequestDto;
import com.jmjbrothers.doctorsappointmentsystem.model.Doctor;
import com.jmjbrothers.doctorsappointmentsystem.model.Patient;
import com.jmjbrothers.doctorsappointmentsystem.model.User;
import com.jmjbrothers.doctorsappointmentsystem.service.AppointmentService;
import com.jmjbrothers.doctorsappointmentsystem.service.DoctorService;
import com.jmjbrothers.doctorsappointmentsystem.service.PatientService;
import com.jmjbrothers.doctorsappointmentsystem.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class UserController {

    private final DoctorService doctorService;
    private final UserService userService;
    private final PatientService patientService;

    private final AppointmentService appointmentService;

    public UserController(DoctorService doctorService, UserService userService, PatientService patientService, AppointmentService appointmentService) {
        this.doctorService = doctorService;
        this.userService = userService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }

    //Doctor Registration by Admin
    @PostMapping("/doctor/register")
    public ResponseEntity<?> registerCompany(@RequestBody DoctorRegisterRequestDto request) {
        Doctor doctor = doctorService.registerNewDoctor(request);
        return new ResponseEntity<>(doctor, HttpStatus.CREATED);
    }

    @GetMapping("/get/all/doctors")
    public ResponseEntity<?> getAllDoctors() {
        List<DoctorDto> doctors = doctorService.getAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/get/all/patients")
    public ResponseEntity<?> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping("/get/all/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/get/all/app/list")
    public ResponseEntity<?> getAllAppointmentByDoctorId() {
        List<AppointmentResponseDto> appointmentList = appointmentService.getAllAppointment();
        return new ResponseEntity<>(appointmentList, HttpStatus.OK);
    }
}
