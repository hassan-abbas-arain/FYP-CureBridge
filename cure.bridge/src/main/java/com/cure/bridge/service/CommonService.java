package com.cure.bridge.service;

import com.cure.bridge.request.DPRequest;
import com.cure.bridge.request.Request;
import com.cure.bridge.response.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CommonService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String EX_API_URL = "http://127.0.0.1:5000/symptom-extraction";
    private static final String SP_API_URL = "http://127.0.0.1:5000/start-session";
    private static final String DP_API_URL = "http://127.0.0.1:5000/select-symptoms/1";

    public Response baseCall(Request request) {
        Response response = new Response();
        String apiUrl = null;

        // Determine the API URL based on the flag
        switch (request.getFlag()) {
            case "EX":
                apiUrl = EX_API_URL;
                break;
            case "SP":
                apiUrl = SP_API_URL;
                break;
            case "DP":
                apiUrl = DP_API_URL;
                break;
            default:
                response.setResponseCode("99");
                response.setResponseMessage("Invalid flag");
                return response;
        }

        try {
            // Make the API call
            HttpEntity<?> httpEntity = null;
            ResponseEntity<Response> apiResponse = null;
            if(apiUrl.equals(DP_API_URL)) {
                DPRequest dpRequest = new DPRequest();
                dpRequest.setSelected_indices(request.getSelected_indices());
                dpRequest.setAction(request.getAction());
                httpEntity = new HttpEntity<>(dpRequest);
                apiResponse = restTemplate.exchange(
                        apiUrl,
                        HttpMethod.POST,
                        httpEntity,
                        Response.class
                );
            }else {
                httpEntity = new HttpEntity<>(request);
                apiResponse = restTemplate.exchange(
                        apiUrl,
                        HttpMethod.POST,
                        httpEntity,
                        Response.class
                );
            }
            // Return the response from the Python API
            if (apiResponse.getBody() != null) {
                response.setResponseCode("00");
                response.setResponseMessage(apiResponse.getBody().getMessage());
                response = apiResponse.getBody();
            } else {
                response.setResponseCode("01");
                response.setResponseMessage("Empty response from API");
            }
        } catch (Exception e) {
            response.setResponseCode("02");
            response.setResponseMessage("Error calling API: " + e.getMessage());
        }

        return response;
    }
}
