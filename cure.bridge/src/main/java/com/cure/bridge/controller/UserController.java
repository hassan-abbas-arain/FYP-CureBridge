package com.cure.bridge.controller;

import com.cure.bridge.entity.*;
import com.cure.bridge.repo.DoctorRepo;
import com.cure.bridge.repo.PatientRepo;
import com.cure.bridge.service.DoctorService;
import com.cure.bridge.service.PatientService;
import com.cure.bridge.service.UserService;
import com.cure.bridge.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final Logger logger =  LoggerFactory.getLogger(UserController.class);
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

  /*  @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = request.get("email");
            String password = request.get("password");
            String fullName = request.get("fullName");
            String roleStr = request.get("role");
            String specialization = request.get("specialization");
            String licenseNumber = request.get("licenseNumber");
            String experienceYearsStr = request.get("experienceYears");
            String contactNumber = request.get("contactNumber");
            String clinicAddress = request.get("clinicAddress");
            String degreePdfPath = request.get("degreePdfPath");
            String specializationList = request.get("specializationList");

            // Validate required fields
            if (email == null || password == null || fullName == null || roleStr == null) {
                response.put("responseCode", "01");
                response.put("responseMessage", "Email, password, fullName, and role are required");
                response.put("data", null);
                return ResponseEntity.badRequest().body(response);
            }

            // Check if user already exists
            if (doctorService.existsUserByEmail(email)) {
                response.put("responseCode", "01");
                response.put("responseMessage", "User with this email already exists");
                response.put("data", null);
                return ResponseEntity.badRequest().body(response);
            }

            // Create new user
            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setFullName(fullName);
            user.setRole(Role.valueOf(roleStr.toUpperCase()));

            // Save user to the database
            user = doctorService.saveUser(user);

            // If the role is DOCTOR, save to Doctor table as well
            if (user.getRole() == Role.DOCTOR) {
                if (specialization == null || licenseNumber == null || experienceYearsStr == null ||
                        contactNumber == null || clinicAddress == null || degreePdfPath == null ||
                        specializationList == null) {
                    response.put("responseCode", "01");
                    response.put("responseMessage", "All doctor fields (specialization, licenseNumber, experienceYears, contactNumber, clinicAddress, degreePdfPath, specializationList) are required");
                    response.put("data", null);
                    return ResponseEntity.badRequest().body(response);
                }

                int experienceYears = Integer.parseInt(experienceYearsStr);

                Doctor doctor = new Doctor();
                doctor.setUser(user);
                doctor.setSpecialization(specialization);
                doctor.setLicenseNumber(licenseNumber);
                doctor.setExperienceYears(experienceYears);
                doctor.setContactNumber(contactNumber);
                doctor.setClinicAddress(clinicAddress);
                doctor.setDegreePdfPath(degreePdfPath);
                doctor.setSpecializationList(specializationList);
                doctor.setStatus(DoctorStatus.PENDING);

                doctorService.saveDoctor(doctor);
            }

            // Prepare success response
            Map<String, Object> data = new HashMap<>();
            data.put("id", user.getId());
            data.put("email", user.getEmail());
            data.put("fullName", user.getFullName());
            data.put("role", user.getRole().toString());
            response.put("responseCode", "00");
            response.put("responseMessage", "User registered successfully");
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("responseCode", "01");
            response.put("responseMessage", "Invalid role value");
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("responseCode", "01");
            response.put("responseMessage", "Registration failed: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }*/

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = request.get("email");
            String password = request.get("password");
            String fullName = request.get("fullName");
            String roleStr = request.get("role");
            String specialization = request.get("specialization");
            String licenseNumber = request.get("licenseNumber");
            String experienceYearsStr = request.get("experienceYears");
            String contactNumber = request.get("contactNumber");
            String clinicAddress = request.get("clinicAddress");
            String degreePdfPath = request.get("degreePdfPath");
            String specializationList = request.get("specializationList");
            String ageStr = request.get("age"); // New field for patient
            String genderStr = request.get("gender"); // New field for patient
            String medicalHistory = request.get("medicalHistory"); // New field for patient

            // Validate required fields
            if (email == null || password == null || fullName == null || roleStr == null) {
                response.put("responseCode", "01");
                response.put("responseMessage", "Email, password, fullName, and role are required");
                response.put("data", null);
                return ResponseEntity.badRequest().body(response);
            }

            // Check if user already exists
            if (doctorService.existsUserByEmail(email)) {
                response.put("responseCode", "01");
                response.put("responseMessage", "User with this email already exists");
                response.put("data", null);
                return ResponseEntity.badRequest().body(response);
            }

            // Create new user
            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setFullName(fullName);
            user.setRole(Role.valueOf(roleStr.toUpperCase()));

            // Save user to the database
            user = doctorService.saveUser(user);

            // Handle role-specific logic
            if (user.getRole() == Role.DOCTOR) {
                if (specialization == null || licenseNumber == null || experienceYearsStr == null ||
                        contactNumber == null || clinicAddress == null || degreePdfPath == null ||
                        specializationList == null) {
                    response.put("responseCode", "01");
                    response.put("responseMessage", "All doctor fields are required");
                    response.put("data", null);
                    return ResponseEntity.badRequest().body(response);
                }

                int experienceYears = Integer.parseInt(experienceYearsStr);

                Doctor doctor = new Doctor();
                doctor.setUser(user);
                doctor.setSpecialization(specialization);
                doctor.setLicenseNumber(licenseNumber);
                doctor.setExperienceYears(experienceYears);
                doctor.setContactNumber(contactNumber);
                doctor.setClinicAddress(clinicAddress);
                doctor.setDegreePdfPath(degreePdfPath);
                doctor.setSpecializationList(specializationList);
                doctor.setStatus(DoctorStatus.PENDING);

                doctorService.saveDoctor(doctor);
            } else if (user.getRole() == Role.PATIENT) {
                if (ageStr == null || genderStr == null || contactNumber == null) {
                    response.put("responseCode", "01");
                    response.put("responseMessage", "Age, gender, and contactNumber are required for patients");
                    response.put("data", null);
                    return ResponseEntity.badRequest().body(response);
                }

                int age = Integer.parseInt(ageStr);
                Gender gender = Gender.valueOf(genderStr.toUpperCase());

                Patient patient = new Patient();
                patient.setUser(user);
                patient.setAge(age);
                patient.setGender(gender);
                patient.setContactNumber(contactNumber);
                patient.setMedicalHistory(medicalHistory); // Optional field

                patientService.savePatient(patient); // Assuming doctorService has this method
            }

            // Prepare success response
            Map<String, Object> data = new HashMap<>();
            data.put("id", user.getId());
            data.put("email", user.getEmail());
            data.put("fullName", user.getFullName());
            data.put("role", user.getRole().toString());
            response.put("responseCode", "00");
            response.put("responseMessage", "User registered successfully");
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("responseCode", "01");
            response.put("responseMessage", "Invalid role or gender value");
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("responseCode", "01");
            response.put("responseMessage", "Registration failed: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = request.get("email");
            String password = request.get("password");

            if (email == null || password == null) {
                response.put("responseCode", "01");
                response.put("responseMessage", "Email and password are required");
                response.put("data", null);
                return ResponseEntity.badRequest().body(response);
            }

            User user = doctorService.findUserByEmail(email);
            if(user==null){
                new Throwable(Constants.DATA_NOT_FOUND_MESSAGE);
            }
            String pass = passwordEncoder.encode(user.getPassword());
            System.out.println(pass);
            if (!passwordEncoder.matches(password, user.getPassword())) {
                response.put("responseCode", "01");
                response.put("responseMessage", "Invalid email or password");
                response.put("data", null);
                return ResponseEntity.badRequest().body(response);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("id", user.getId());
            data.put("email", user.getEmail());
            data.put("fullName", user.getFullName());
            data.put("role", user.getRole().toString());
            response.put("responseCode", "00");
            response.put("responseMessage", "Login successful");
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("responseCode", "01");
            response.put("responseMessage", "Login failed: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Map<String, Object>> getUserDetails(@PathVariable Long id) {
        try {
            Optional<User> optionalUser = userService.getByUserId(id);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("responseCode", "01", "responseMessage", "User not found"));
            }

            User user = optionalUser.get();
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("fullName", user.getFullName());
            userInfo.put("email", user.getEmail());
            userInfo.put("role", user.getRole());
            if(Role.PATIENT.equals(user.getRole())){
                Patient patient = patientService.patientRepo.findByUserId(user.getId());
                userInfo.put("status",patient.getStatus());
            }
            if (Role.DOCTOR.equals(user.getRole())) {
                Doctor doctor = doctorRepo.findByUserId(user.getId());
                if (doctor != null) {
                    userInfo.put("status",doctor.getStatus());
                    userInfo.put("specialization", doctor.getSpecialization());
                    userInfo.put("license", doctor.getLicenseNumber());
                }
            }

            logger.info(Objects.toString(userInfo));
            return ResponseEntity.ok(Map.of(
                    "responseCode", "00",
                    "responseMessage", "Success",
                    "data", userInfo
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("responseCode", "99", "responseMessage", "Server Error"));
        }
    }

    @PutMapping("/user/{id}/status")
    public ResponseEntity<Map<String, Object>> updateUserStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody) {
        try {
            String newStatus = requestBody.get("status");

            Optional<User> optionalUser = userService.getByUserId(id);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("responseCode", "01", "responseMessage", "User not found"));
            }

            User user = optionalUser.get();
            if(Role.PATIENT.equals(user.getRole())){
                Patient patient = patientService.patientRepo.findByUserId(user.getId());
                patient.setStatus(PatientStatus.valueOf(newStatus));
                patientService.savePatient(patient);
            }
            if (Role.DOCTOR.equals(user.getRole())) {
                Doctor doctor = doctorRepo.findByUserId(user.getId());
                doctor.setStatus(DoctorStatus.valueOf(newStatus));
                doctorService.saveDoctor(doctor);
            }

            return ResponseEntity.ok(Map.of(
                    "responseCode", "00",
                    "responseMessage", "User status updated successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("responseCode", "99", "responseMessage", "Server error"));
        }
    }

}