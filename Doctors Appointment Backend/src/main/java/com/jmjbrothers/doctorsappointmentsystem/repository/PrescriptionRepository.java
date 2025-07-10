package com.jmjbrothers.doctorsappointmentsystem.repository;

import com.jmjbrothers.doctorsappointmentsystem.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findAllByPatient_Id(Long patientId);
    List<Prescription> findAllByDoctor_Id(Long doctorId);

    Prescription findByAppointmentId(Long id);
}
