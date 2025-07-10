package com.jmjbrothers.doctorsappointmentsystem.controller;


import com.jmjbrothers.doctorsappointmentsystem.dto.AppointmentResponseDto;
import com.jmjbrothers.doctorsappointmentsystem.dto.PrescriptionDto;
import com.jmjbrothers.doctorsappointmentsystem.dto.PrescriptionResponseDto;
import com.jmjbrothers.doctorsappointmentsystem.dto.UpdateAppointmentStatusRequest;
import com.jmjbrothers.doctorsappointmentsystem.model.Appointment;
import com.jmjbrothers.doctorsappointmentsystem.model.Prescription;
import com.jmjbrothers.doctorsappointmentsystem.service.AppointmentService;
import com.jmjbrothers.doctorsappointmentsystem.service.PrescriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final PrescriptionService prescriptionService;
    private final AppointmentService appointmentService;


    public DoctorController(PrescriptionService prescriptionService, AppointmentService appointmentService) {
        this.prescriptionService = prescriptionService;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/create/prescription")
    public ResponseEntity<?> createPrescription(@RequestBody PrescriptionDto prescriptionDto) {
        Prescription preserveDto = prescriptionService.createPrescription(prescriptionDto);
        return new ResponseEntity<>(preserveDto, HttpStatus.CREATED);
    }

    @GetMapping("/get/all/prescriptions/by/doc/me")
    public ResponseEntity<?> getPrescriptionByDoctorId(@RequestParam Long id) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionByDoctorId(id);
        return new ResponseEntity<>(prescriptions, HttpStatus.OK);
    }

    @GetMapping("/get/prescription/by/app")
    public ResponseEntity<?> getPrescriptionByAppId(@RequestParam Long id) {
        PrescriptionResponseDto prescriptionByAppId = prescriptionService.getPrescriptionByAppId(id);
        return new ResponseEntity<>(prescriptionByAppId, HttpStatus.OK);
    }

    @GetMapping("/get/all/app/list/by/me")
    public ResponseEntity<?> getAllAppointmentByDoctorId(@RequestParam("id") Long id) {
        List<AppointmentResponseDto> appointmentList = appointmentService.getAllAppointmentByDoctorId(id);
        return new ResponseEntity<>(appointmentList, HttpStatus.OK);
    }

    @PutMapping("/update/appointment/status")
    public ResponseEntity<?> updateStatus(@RequestBody UpdateAppointmentStatusRequest request) {
        Appointment updated = appointmentService.updateAppointmentStatus(request);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
}
