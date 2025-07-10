package com.jmjbrothers.doctorsappointmentsystem.service;

import com.jmjbrothers.doctorsappointmentsystem.dto.UserRegisterRequestDto;
import com.jmjbrothers.doctorsappointmentsystem.model.Doctor;
import com.jmjbrothers.doctorsappointmentsystem.model.User;
import com.jmjbrothers.doctorsappointmentsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User registerNewUser(UserRegisterRequestDto request) {

        User existUser = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (existUser != null){
            throw new RuntimeException("User already exist by email: "+request.getEmail());
        }

        User user =  new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User saveUser =  userRepository.save(user);

        if (saveUser != null){
            return saveUser;
        }else {
            return null;
        }
    }

    //Get all users by admin
    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
