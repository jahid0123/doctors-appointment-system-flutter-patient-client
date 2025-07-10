package com.jmjbrothers.doctorsappointmentsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jmjbrothers.doctorsappointmentsystem.common.PortalUser;
import com.jmjbrothers.doctorsappointmentsystem.constants.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bithy_doctor")
public class Doctor implements PortalUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_name", nullable = false)
    private String name;

    @Column(name = "doctor_email", unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @Column(name = "doctor_password", nullable = false)
    private String password;

    private String specialization;

    private String qualification;

    private int experience;

    private String hospitalName;

    private String phone;

    private String image;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private Role role = Role.DOCTOR;

    private LocalDateTime createdAt = LocalDateTime.now();
}
