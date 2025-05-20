package com.cure.bridge.service;

import com.cure.bridge.entity.Appointment;
import com.cure.bridge.entity.Doctor;
import com.cure.bridge.entity.Patient;
import com.cure.bridge.entity.User;
import com.cure.bridge.repo.AppointmentRepo;
import com.cure.bridge.repo.DoctorRepo;
import com.cure.bridge.repo.PatientRepo;
import com.cure.bridge.repo.UserRepo;
import com.cure.bridge.response.Response;
import com.cure.bridge.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepo appointmentRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private DoctorRepo doctorRepo;

    public Response appointments(Long id){
        Response response = new Response();
        List<Appointment> appointments = new ArrayList<>();
        User user = userRepo.findById(id).
                orElseThrow(() -> new RuntimeException("User not found"));
        Patient patient = patientRepo.findByUserId(id);
        if(patient!=null) {
            appointments = appointmentRepo.findByPatientId(patient.getId());
        }else{
            Doctor doctor = doctorRepo.findByUserId(user.getId());
            appointments = appointmentRepo.findByDoctorId(doctor.getId());
        }
        if(appointments.isEmpty()){
            response.setResponseCode(Constants.DATA_NOT_FOUND_CODE);
            response.setResponseMessage(Constants.DATA_NOT_FOUND_MESSAGE);
        }else {
            response.setResponseCode(Constants.SUCCESS_CODE);
            response.setResponseMessage(Constants.SUCCESS_MESSAGE);
            Map<String,List> map = new HashMap<>();
            map.put("appointments",appointments);
            response.setData(map);
        }

        return response;
    }
}
