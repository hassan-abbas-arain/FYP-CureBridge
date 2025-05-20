package com.cure.bridge.service;

import com.cure.bridge.entity.Doctor;
import com.cure.bridge.entity.DoctorStatus;
import com.cure.bridge.entity.Role;
import com.cure.bridge.entity.User;
import com.cure.bridge.repo.DoctorRepo;
import com.cure.bridge.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepo doctorRepo;
    @Autowired
    private UserRepo userRepo;



    public List<Doctor> listOfAllDoctor(){
        return (List<Doctor>) doctorRepo.findAll();
    }
    public List<Doctor> getPendingDoctors() {
        return doctorRepo.findByStatus(DoctorStatus.PENDING);
    }

    public Doctor updateDoctorStatus(Long doctorId, String action, Long adminId) {
        Doctor doctor = doctorRepo.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        if ("approve".equalsIgnoreCase(action)) {
            doctor.setStatus(DoctorStatus.ACTIVE);
        } else if ("reject".equalsIgnoreCase(action)) {
            doctor.setStatus(DoctorStatus.INACTIVE);
        } else {
            throw new IllegalArgumentException("Invalid action");
        }
        User user = userRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        doctor.setVerifiedBy(user);
        doctor.setVerifiedAt(Timestamp.valueOf(LocalDateTime.now()));
        return doctorRepo.save(doctor);
    }

    public List<User> getActiveUsersByRole(String role) {
        Role userRole = Role.valueOf(role.toUpperCase());
        return userRepo.findByRole(userRole);
    }
    public User saveUser(User user){
        return userRepo.save(user);
    }

    public Doctor saveDoctor(Doctor doctor){
        return doctorRepo.save(doctor);
    }

    public Boolean existsUserByEmail(String email){
        User user = userRepo.findByEmail(email);
        if(user!=null){
            return true;
        }
        return false;
    }

    public User findUserByEmail(String email){
        User user = userRepo.findByEmail(email);
        if(user!=null){
            return user;
        }
        return null;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepo.findAll();
    }
}
