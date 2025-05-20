package com.cure.bridge.service;

import com.cure.bridge.entity.Doctor;
import com.cure.bridge.entity.Patient;
import com.cure.bridge.entity.Role;
import com.cure.bridge.entity.User;
import com.cure.bridge.repo.DoctorRepo;
import com.cure.bridge.repo.PatientRepo;
import com.cure.bridge.repo.UserRepo;
import com.cure.bridge.response.Response;
import com.cure.bridge.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private DoctorRepo doctorRepo;

    public Response getAllUsers(){
        Response response = new Response();
        try{
            List<User> userlist = userRepo.findAllByRoleIn(List.of(Role.DOCTOR, Role.PATIENT));
            if(userlist.isEmpty()){
                response.setResponseCode(Constants.DATA_NOT_FOUND_CODE);
                response.setResponseMessage(Constants.DATA_NOT_FOUND_MESSAGE);
            }else{
                response.setResponseCode(Constants.SUCCESS_CODE);
                response.setResponseMessage(Constants.SUCCESS_MESSAGE);
                List<User> newUserList = new ArrayList<>();
                for(User user:userlist){
                    if(Role.PATIENT.equals(user.getRole())){
                        Patient patient = patientRepo.findByUserId(user.getId());
                        user.setStatus(String.valueOf(patient.getStatus()));
                        newUserList.add(user);
                    }else if (Role.DOCTOR.equals(user.getRole())){
                        Doctor doctor = doctorRepo.findByUserId(user.getId());
                        user.setStatus(String.valueOf(doctor.getStatus()));
                        newUserList.add(user);
                    }
                }
                Map<String,List> map = new HashMap<>();
                map.put("users",newUserList);
                response.setData(map);
            }
        }catch (Exception e ){
            e.printStackTrace();
        }

        return response;
    }

    public Optional<User> getByUserId(long id){
        return userRepo.findById(id);
    }

}
