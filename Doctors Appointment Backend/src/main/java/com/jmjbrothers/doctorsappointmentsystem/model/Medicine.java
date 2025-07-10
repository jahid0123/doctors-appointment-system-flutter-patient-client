package com.jmjbrothers.doctorsappointmentsystem.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Medicine {
    private String name;
    private String dosage;
    private String frequency;
    private String duration;    // e.g., "5 days"
}