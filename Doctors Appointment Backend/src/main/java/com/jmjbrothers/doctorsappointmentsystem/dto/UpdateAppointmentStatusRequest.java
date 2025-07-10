package com.jmjbrothers.doctorsappointmentsystem.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UpdateAppointmentStatusRequest {

    private Long appointmentId;
    @Enumerated(EnumType.STRING)
    private String status; // Should be "CONFIRMED" or "REJECTED"
}
