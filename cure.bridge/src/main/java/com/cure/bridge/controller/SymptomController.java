package com.cure.bridge.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class SymptomController {

    private final Logger logger = LoggerFactory.getLogger(SymptomController.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final String PYTHON_SERVER_BASE_URL = "http://localhost:5000";
    private final String PYTHON_SENTIMENT_BASE_URL = "http://localhost:8000";

    private String sessionId = null;

    @PostMapping("/process")
    public ResponseEntity<?> handleSymptomFlow(@RequestBody Map<String, Object> payload) {
        logger.info("Received /process request with payload: {}", payload);
        String flag = (String) payload.get("flag");
        ResponseEntity<?> response;

        switch (flag) {
            case "EX":
                response = extractSymptoms(payload);
                break;
            case "SP":
                response = startSession(payload);
                break;
            case "DP":
                response = selectOrPredict(payload);
                break;
            default:
                logger.warn("Invalid flag received: {}", flag);
                return ResponseEntity.badRequest().body("Invalid flag");
        }

        logger.info("Response for flag [{}]: {}", flag, response.getBody());
        return response;
    }

    private ResponseEntity<?> extractSymptoms(Map<String, Object> payload) {
        String url = PYTHON_SERVER_BASE_URL + "/symptom-extraction";
        logger.info("Calling Python API: POST {}", url);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        logger.info("Response from /symptom-extraction: {}", response.getBody());
        return ResponseEntity.ok(response.getBody());
    }

    private ResponseEntity<?> startSession(Map<String, Object> payload) {
        String url = PYTHON_SERVER_BASE_URL + "/start-session";
        logger.info("Calling Python API: POST {}", url);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        logger.info("Response from /start-session: {}", response.getBody());

        Map<String, Object> body = response.getBody();
        if (body != null && body.containsKey("session_id")) {
            sessionId = String.valueOf(body.get("session_id"));
            logger.info("Stored session ID: {}", sessionId);
        } else {
            logger.warn("No session ID found in response.");
        }

        return ResponseEntity.ok(body);
    }

    private ResponseEntity<?> selectOrPredict(Map<String, Object> payload) {
        if (sessionId == null) {
            logger.warn("Session ID is null. Cannot proceed with DP flag.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Session not started.");
        }

        String action = (String) payload.get("action");
        String url;

        if ("no".equalsIgnoreCase(action)) {
            url = PYTHON_SERVER_BASE_URL + "/predict-disease/" + sessionId;
        } else {
            url = PYTHON_SERVER_BASE_URL + "/select-symptoms/" + sessionId;
        }

        logger.info("Calling Python API: POST {}", url);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        logger.info("Response from {}: {}", url, response.getBody());
        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/sentiment")
    public ResponseEntity<?> predictSentiment(@RequestBody Map<String, Object> payload) {
        logger.info("Received /sentiment request with payload: {}", payload);

        // Validate payload
        if (!payload.containsKey("text") || payload.get("text") == null) {
            logger.warn("Missing or null 'text' field in payload");
            return ResponseEntity.badRequest().body("Missing 'text' field in payload");
        }

        String url = PYTHON_SENTIMENT_BASE_URL + "/predict";
        logger.info("Calling FastAPI sentiment analysis API: POST {}", url);

        // Create request entity with payload
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload);
        try {
            // Send POST request to FastAPI /predict endpoint
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            logger.info("Response from /predict: {}", response.getBody());
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            logger.error("Error calling sentiment analysis API: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error calling sentiment analysis service: " + e.getMessage());
        }
    }

}
