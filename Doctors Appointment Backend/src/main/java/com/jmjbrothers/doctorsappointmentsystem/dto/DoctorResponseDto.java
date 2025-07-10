package com.jmjbrothers.doctorsappointmentsystem.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DoctorResponseDto {
    private String name;
    private String specialization;
    private String qualification;
    private int experience;
    private String hospitalName;
    private String phone;
}