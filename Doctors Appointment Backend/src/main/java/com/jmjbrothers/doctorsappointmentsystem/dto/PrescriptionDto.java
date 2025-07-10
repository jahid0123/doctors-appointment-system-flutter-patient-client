package com.jmjbrothers.doctorsappointmentsystem.dto;

import com.jmjbrothers.doctorsappointmentsystem.model.Appointment;
import com.jmjbrothers.doctorsappointmentsystem.model.Doctor;
import com.jmjbrothers.doctorsappointmentsystem.model.Medicine;
import com.jmjbrothers.doctorsappointmentsystem.model.Patient;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PrescriptionDto {

    private String symptoms;
    private String diagnosis;
    private List<Medicine> medicines;
    private Long appointmentId;
    private Long doctorId;
    private Long patientId;
}
