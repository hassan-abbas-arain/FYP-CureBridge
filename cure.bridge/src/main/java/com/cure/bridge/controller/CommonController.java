package com.cure.bridge.controller;

import com.cure.bridge.entity.Doctor;
import com.cure.bridge.request.Request;
import com.cure.bridge.response.Response;
import com.cure.bridge.service.CommonService;
import com.cure.bridge.service.UserService;
import jdk.jshell.execution.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class CommonController {
    private final Logger logger = LoggerFactory.getLogger(CommonController.class);
    @Autowired
    private CommonService commonService;
    @Autowired
    private UserService userServicel;
    @PostMapping(value = "/disease/prediction")
    public Response diseasePrediction(@RequestBody Request request){
        Response response = new Response();
        if(!request.getFlag().isEmpty()){
            response = commonService.baseCall(request);
        }

        return response;
    }

    @GetMapping("/getallusers")
    public Response getAllDoctor() throws Exception{
        Response response = new Response();
        try{
            response = userServicel.getAllUsers();
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
}
