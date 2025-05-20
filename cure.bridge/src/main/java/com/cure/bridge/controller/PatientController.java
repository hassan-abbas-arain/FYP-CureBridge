package com.cure.bridge.controller;

import com.cure.bridge.entity.Appointment;
import com.cure.bridge.entity.Doctor;
import com.cure.bridge.response.Response;
import com.cure.bridge.service.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PatientController {
    private final Logger logger = LoggerFactory.getLogger(PatientController.class);
    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/getappointment")
    public Response getAppointment(@RequestParam long id) throws Exception{
        Response response = new Response();
        try{
            response = appointmentService.appointments(id);
            logger.info(Objects.toString(response));
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
}
