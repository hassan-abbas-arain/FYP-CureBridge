package com.cure.bridge.service;

import com.cure.bridge.entity.Patient;
import com.cure.bridge.repo.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    @Autowired
    public PatientRepo patientRepo ;

    public Patient savePatient(Patient patient){
        return patientRepo.save(patient);
    }
}
