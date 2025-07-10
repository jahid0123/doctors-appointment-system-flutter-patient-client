package com.jmjbrothers.doctorsappointmentsystem.service.auth;

import com.jmjbrothers.doctorsappointmentsystem.config.UserInfoDetails;
import com.jmjbrothers.doctorsappointmentsystem.model.Doctor;
import com.jmjbrothers.doctorsappointmentsystem.model.Patient;
import com.jmjbrothers.doctorsappointmentsystem.model.User;
import com.jmjbrothers.doctorsappointmentsystem.repository.DoctorRepository;
import com.jmjbrothers.doctorsappointmentsystem.repository.PatientRepository;
import com.jmjbrothers.doctorsappointmentsystem.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoDetailsService implements UserDetailsService {


    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    public UserInfoDetailsService(DoctorRepository doctorRepository, UserRepository userRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Patient> patient = patientRepository.findByEmail(email);
        if (patient.isPresent()) {
            Patient p = patient.get();
            return new UserInfoDetails(p);
        }

        Optional<Doctor> doctor = doctorRepository.findByEmail(email);
        if (doctor.isPresent()) {
            Doctor d = doctor.get();
            return new UserInfoDetails(d);
        }

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User u = user.get();
            return new UserInfoDetails(u);
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }

}

