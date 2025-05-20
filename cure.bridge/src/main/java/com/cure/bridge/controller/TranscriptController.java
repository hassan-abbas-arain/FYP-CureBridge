package com.cure.bridge.controller;


import com.cure.bridge.entity.Transcript;
import com.cure.bridge.repo.TranscriptRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TranscriptController {
    private final Logger logger = LoggerFactory.getLogger(TranscriptController.class);


    @Autowired
    private TranscriptRepository transcriptRepository;

    @PostMapping("/save-transcript")
    public ResponseEntity<String> saveTranscript(@RequestBody Transcript transcript) {
        try {
            logger.info(Objects.toString(transcript));
            transcriptRepository.save(transcript);
            return ResponseEntity.ok("Transcript saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving transcript: " + e.getMessage());
        }
    }
    @GetMapping("/transcript/{appointmentId}")
    public ResponseEntity<?> getTranscriptByAppointmentId(@PathVariable String appointmentId) {
        try {
            Optional<Transcript> optionalTranscript = transcriptRepository.findByAppointmentId(appointmentId);
            if (optionalTranscript.isPresent()) {
                Transcript transcript = optionalTranscript.get();
                return ResponseEntity.ok(Map.of("transcript", transcript.getTranscript()));
            } else {
                return ResponseEntity.status(404).body("Transcript not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

}