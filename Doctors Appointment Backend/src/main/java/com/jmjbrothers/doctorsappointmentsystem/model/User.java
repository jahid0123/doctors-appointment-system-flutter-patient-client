package com.jmjbrothers.doctorsappointmentsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jmjbrothers.doctorsappointmentsystem.common.PortalUser;
import com.jmjbrothers.doctorsappointmentsystem.constants.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bithy_user")
public class User implements PortalUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private Role role = Role.ADMIN;

    @JsonIgnore
    @Column(name = "phone")
    private String phone;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }


}
