package com.jmjbrothers.doctorsappointmentsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jmjbrothers.doctorsappointmentsystem.common.PortalUser;
import com.jmjbrothers.doctorsappointmentsystem.constants.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bithy_patient")
public class Patient implements PortalUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_name", nullable = false)
    private String name;

    @Column(name = "patient_email", unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @Column(name = "patient_password", nullable = false)
    private String password;

    @Column(name = "patient_phone")
    private String phone;

    @Column(name = "patient_gender", nullable = false)
    private String gender;

    @Column(name = "patient_date_of_birth", nullable = false)
    private LocalDate dob;

    @Column(name = "patient_address")
    private String address;

    // Optional: to read discriminator column
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role = Role.PATIENT;
}
