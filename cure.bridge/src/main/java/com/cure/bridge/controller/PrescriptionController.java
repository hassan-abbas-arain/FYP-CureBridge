package com.cure.bridge.controller;

import com.cure.bridge.entity.Appointment;
import com.cure.bridge.entity.Prescription;
import com.cure.bridge.repo.AppointmentRepo;
import com.cure.bridge.repo.PrescriptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionRepo prescriptionRepository;

    @PostMapping
    public ResponseEntity<?> createPrescription(@RequestBody Map<String, Object> body) {
        try {
            Prescription prescription = new Prescription();
            prescription.setAppointmentId(Long.parseLong(body.get("appointmentId").toString()));
            prescription.setDoctorId(Long.parseLong(body.get("doctorId").toString()));
            prescription.setPatientId(Long.parseLong(body.get("patientId").toString()));
            prescription.setDiagnosis(body.get("diagnosis").toString());
            prescription.setMedicines(body.get("medicines").toString());

            prescriptionRepository.save(prescription);
            return ResponseEntity.ok("Prescription saved");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving prescription: " + e.getMessage());
        }
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<?> getPrescription(@PathVariable Long appointmentId) {
        Optional<Prescription> prescription = prescriptionRepository.findByAppointmentId(appointmentId);

        if (prescription.isPresent()) {
            return ResponseEntity.ok(prescription.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No prescription found");
        }
    }


}
