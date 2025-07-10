package com.jmjbrothers.doctorsappointmentsystem.service;

import com.jmjbrothers.doctorsappointmentsystem.constants.Status;
import com.jmjbrothers.doctorsappointmentsystem.dto.AppointmentDto;
import com.jmjbrothers.doctorsappointmentsystem.dto.AppointmentResponseDto;
import com.jmjbrothers.doctorsappointmentsystem.dto.UpdateAppointmentStatusRequest;
import com.jmjbrothers.doctorsappointmentsystem.model.Appointment;
import com.jmjbrothers.doctorsappointmentsystem.model.Doctor;
import com.jmjbrothers.doctorsappointmentsystem.model.Patient;
import com.jmjbrothers.doctorsappointmentsystem.repository.AppointmentRepository;
import com.jmjbrothers.doctorsappointmentsystem.repository.DoctorRepository;
import com.jmjbrothers.doctorsappointmentsystem.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;


    public AppointmentService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }


    @Transactional
    public Appointment bookAppointmentByPatient(AppointmentDto appointmentDto) {
        Doctor doctor = doctorRepository.findById(appointmentDto.getDoctorId()).orElseThrow(
                () -> new RuntimeException("Doctor not found")
        );
        Patient patient = patientRepository.findById(appointmentDto.getPatientId()).orElseThrow(
                () -> new RuntimeException("Patient not found")
        );

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setAppointmentTime(appointmentDto.getAppointmentTime());

        return appointmentRepository.save(appointment);

    }
    @Transactional
    public List<AppointmentResponseDto> getAllAppointmentByDoctorId(Long doctorId) {

        List<Appointment> appointmentList  = appointmentRepository.findAllByDoctor_Id(doctorId);
        return appointmentList.stream().map(this:: mapAppointmentResponseDto).collect(Collectors.toList());
    }

    @Transactional
    public List<AppointmentResponseDto> getAllAppointmentByPatientId(Long patientId) {

        List<Appointment> appointmentList  = appointmentRepository.findAllByPatient_Id(patientId);
        return appointmentList.stream().map(this:: mapAppointmentResponseDto).collect(Collectors.toList());
    }

    private AppointmentResponseDto mapAppointmentResponseDto(Appointment appointment) {

        AppointmentResponseDto appointmentResponseDto = new AppointmentResponseDto();

        appointmentResponseDto.setDoctorId(appointment.getDoctor().getId());
        appointmentResponseDto.setDoctorName(appointment.getDoctor().getName());
        appointmentResponseDto.setQualification(appointment.getDoctor().getQualification());
        appointmentResponseDto.setPatientName(appointment.getPatient().getName());
        appointmentResponseDto.setPatientGender(appointment.getPatient().getGender());
        appointmentResponseDto.setPatientDob(appointment.getPatient().getDob());
        appointmentResponseDto.setAppointmentDate(appointment.getAppointmentDate());
        appointmentResponseDto.setAppointmentTime(appointment.getAppointmentTime());
        appointmentResponseDto.setAppointmentStatus(appointment.getStatus());
        appointmentResponseDto.setAppointmentId(appointment.getId());
        appointmentResponseDto.setPatientId(appointment.getPatient().getId());

        return appointmentResponseDto;
    }

    @Transactional
    public Appointment updateAppointmentStatus(UpdateAppointmentStatusRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + request.getAppointmentId()));

        try {
            appointment.setStatus(Status.valueOf(request.getStatus()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + request.getStatus());
        }

        return appointmentRepository.save(appointment);
    }

    public List<AppointmentResponseDto> getAllAppointment() {
        List<Appointment> appointmentList  = appointmentRepository.findAll();
        return appointmentList.stream().map(this::mapAppointmentResponseDto).collect(Collectors.toList());
    }
}