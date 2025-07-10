package com.jmjbrothers.doctorsappointmentsystem.controller.auth;


import com.jmjbrothers.doctorsappointmentsystem.common.PortalUser;
import com.jmjbrothers.doctorsappointmentsystem.config.JwtService;
import com.jmjbrothers.doctorsappointmentsystem.config.UserInfoDetails;
import com.jmjbrothers.doctorsappointmentsystem.dto.DoctorRegisterRequestDto;
import com.jmjbrothers.doctorsappointmentsystem.dto.LoginRequest;
import com.jmjbrothers.doctorsappointmentsystem.dto.PatientRegisterRequestDto;
import com.jmjbrothers.doctorsappointmentsystem.dto.UserRegisterRequestDto;
import com.jmjbrothers.doctorsappointmentsystem.model.Doctor;
import com.jmjbrothers.doctorsappointmentsystem.model.Patient;
import com.jmjbrothers.doctorsappointmentsystem.model.User;
import com.jmjbrothers.doctorsappointmentsystem.service.DoctorService;
import com.jmjbrothers.doctorsappointmentsystem.service.PatientService;
import com.jmjbrothers.doctorsappointmentsystem.service.UserService;
import com.jmjbrothers.doctorsappointmentsystem.service.auth.UserInfoDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {


    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserInfoDetailsService userInfoDetailsService;
    private final UserService userService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager, UserInfoDetailsService userInfoDetailsService, UserService userService, DoctorService doctorService, PatientService patientService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userInfoDetailsService = userInfoDetailsService;
        this.userService = userService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }


        @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequestDto request) {

        User user = userService.registerNewUser(request);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/patient/register")
    public ResponseEntity<?> registerSeeker(@RequestBody PatientRegisterRequestDto request) {

        Patient patient = patientService.registerNewPatient(request);

        return new ResponseEntity<>(patient, HttpStatus.CREATED);
    }


   /* @PostMapping("/doctor/register")
    public ResponseEntity<?> registerCompany(@RequestBody DoctorRegisterRequestDto request) {

        Doctor doctor = doctorService.registerNewDoctor(request);

        return new ResponseEntity<>(doctor, HttpStatus.CREATED);
    }*/


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            if (authentication.isAuthenticated()) {
                final String jwtToken = jwtService.createToken(loginRequest.getEmail());
                UserInfoDetails userInfoDetails = (UserInfoDetails) authentication.getPrincipal();
                PortalUser user = userInfoDetails.user();

                Map<String, Object> responseData = new HashMap<>();
                responseData.put("token", jwtToken);
                responseData.put("id", user.getId());
                responseData.put("role", user.getRole());

                /*responseData.put("token_type", "Bearer");

                Map<String, Object> userData = new HashMap<>();
                userData.put("Name", user.getName());
                userData.put("Email", user.getEmail());
                userData.put("Phone", user.getPhone());
                userData.put("Role", user.getRole());

                responseData.put("user", userData);*/

                return ResponseEntity.ok(responseData);
            } else {
                throw new UsernameNotFoundException("Invalid User or password");
            }
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }


}
