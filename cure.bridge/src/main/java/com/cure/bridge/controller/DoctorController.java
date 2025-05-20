package com.cure.bridge.controller;

import com.cure.bridge.dto.DoctorDTO;
import com.cure.bridge.entity.Doctor;
import com.cure.bridge.entity.User;
import com.cure.bridge.service.DoctorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class DoctorController {
    private final Logger logger = LoggerFactory.getLogger(DoctorController.class);

    @Autowired
    private DoctorService doctorService;
    @GetMapping("/getalldoctor")
    public List<Doctor> getAllDoctor() throws Exception{
        List<Doctor> doctorList = new ArrayList<>();
        try{
            doctorList = doctorService.listOfAllDoctor();
        }catch (Exception e){
            e.printStackTrace();
        }
        return doctorList;
    }

    @GetMapping("/getpendingdoctors")
    public ResponseEntity<Map<String, Object>> getPendingDoctors() {
        List<Doctor> doctors = doctorService.getPendingDoctors();
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("doctors", doctors);
        response.put("responseCode", "00");
        response.put("responseMessage", "Success");
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/updatedoctorstatus/{doctorId}")
    public ResponseEntity<Map<String, Object>> updateDoctorStatus(
            @PathVariable Long doctorId,
            @RequestBody Map<String, String> request,
            @RequestParam Long adminId) {
        String action = request.get("action");
        doctorService.updateDoctorStatus(doctorId, action, adminId);
        Map<String, Object> response = new HashMap<>();
        response.put("responseCode", "00");
        response.put("responseMessage", "Doctor status updated successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getactiveusers")
    public ResponseEntity<Map<String, Object>> getActiveUsersByRole(@RequestParam String role) {
        List<User> users = doctorService.getActiveUsersByRole(role);
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("users", users);
        response.put("responseCode", "00");
        response.put("responseMessage", "Success");
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctors")
    public ResponseEntity<Map<String, Object>> getAllDoctors() {
        try {
            List<Doctor> doctors = doctorService.getAllDoctors();
            List<DoctorDTO > doctorDTOS = new ArrayList<>();
            for(Doctor doctor:doctors){
                DoctorDTO doctorDTO = new DoctorDTO();
                doctorDTO.setId(doctor.getUser().getId());
                doctorDTO.setFullName(doctor.getUser().getFullName());
                doctorDTO.setSpecialization(doctor.getSpecialization());
                doctorDTOS.add(doctorDTO);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("responseCode", "00");
            response.put("responseMessage", "Success");
            response.put("data", doctorDTOS);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("responseCode", "99");
            error.put("responseMessage", "Error fetching doctors");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }



}

