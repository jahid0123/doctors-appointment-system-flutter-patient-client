package com.jmjbrothers.doctorsappointmentsystem.dto;

import com.jmjbrothers.doctorsappointmentsystem.model.Appointment;
import com.jmjbrothers.doctorsappointmentsystem.model.Medicine;
import com.jmjbrothers.doctorsappointmentsystem.model.Prescription;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class PrescriptionResponseDto {

    private Long id;
    private String symptom;
    private String diagnosis;
    private List<Medicine> medicines;
    private LocalDate dateIssued;
    private String patientName;
    private LocalDate patientDob;
    private String doctorName;
    private String doctorQualification;
    private String doctorSpecialization;
    private String doctorHospitalName;

    public PrescriptionResponseDto(Prescription prescription) {
        this.id = prescription.getId();
        this.symptom = prescription.getSymptoms();
        this.diagnosis = prescription.getDiagnosis();
        this.medicines = prescription.getMedicines();
        this.dateIssued = prescription.getDateIssued();
        this.patientName = prescription.getPatient().getName();
        this.patientDob = prescription.getPatient().getDob();
        this.doctorName = prescription.getDoctor().getName();
        this.doctorHospitalName = prescription.getDoctor().getHospitalName();
        this.doctorQualification = prescription.getDoctor().getQualification();
        this.doctorSpecialization = prescription.getDoctor().getSpecialization();
    }
}
