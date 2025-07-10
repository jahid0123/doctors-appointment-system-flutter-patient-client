package com.jmjbrothers.doctorsappointmentsystem.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@RequiredArgsConstructor
public class AppointmentDto {

    private Long doctorId;
    private Long patientId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

}
