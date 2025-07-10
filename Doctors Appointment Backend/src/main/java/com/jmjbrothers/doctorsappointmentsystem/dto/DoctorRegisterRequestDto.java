package com.jmjbrothers.doctorsappointmentsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DoctorRegisterRequestDto {
    private String name;
    private String email;
    private String password;
    private String specialization;
    private String qualification;
    private int experience;
    private String hospitalName;
    private String phone;
}
