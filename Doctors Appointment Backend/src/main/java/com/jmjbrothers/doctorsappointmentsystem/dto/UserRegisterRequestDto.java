package com.jmjbrothers.doctorsappointmentsystem.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserRegisterRequestDto {

    private String name;
    private String email;
    private String password;
}
