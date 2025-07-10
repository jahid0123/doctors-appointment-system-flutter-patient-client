package com.jmjbrothers.doctorsappointmentsystem.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class DoctorDto {

    private Long id;
    private String name;
    private String email;
    private String specialization;
    private String qualification;
    private int experience;
    private String hospitalName;
    private String phone;
    private LocalDateTime createdAt;
}
