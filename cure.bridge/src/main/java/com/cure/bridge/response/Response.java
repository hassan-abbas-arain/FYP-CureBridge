package com.cure.bridge.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Response {
    private String responseCode;
    private String responseMessage;
    private Object[] matching_symptoms;
    private String disease;
    private Object[] given_symptoms;
    private Object[] selected_symptoms;
    private String message;
    private String session_id;
    private Object[] common_symptoms;
    private Object[] new_symptoms;
    private Object[] symptoms;
    private Map<String, List> data;
    private Map<String, Object>  mapData;


    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Object[] getMatching_symptoms() {
        return matching_symptoms;
    }

    public void setMatching_symptoms(Object[] matching_symptoms) {
        this.matching_symptoms = matching_symptoms;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public Object[] getGiven_symptoms() {
        return given_symptoms;
    }

    public void setGiven_symptoms(Object[] given_symptoms) {
        this.given_symptoms = given_symptoms;
    }

    public Object[] getSelected_symptoms() {
        return selected_symptoms;
    }

    public void setSelected_symptoms(Object[] selected_symptoms) {
        this.selected_symptoms = selected_symptoms;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public Object[] getCommon_symptoms() {
        return common_symptoms;
    }

    public void setCommon_symptoms(Object[] common_symptoms) {
        this.common_symptoms = common_symptoms;
    }

    public Object[] getNew_symptoms() {
        return new_symptoms;
    }

    public void setNew_symptoms(Object[] new_symptoms) {
        this.new_symptoms = new_symptoms;
    }

    public Object[] getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(Object[] symptoms) {
        this.symptoms = symptoms;
    }

    public Map<String, List> getData() {
        return data;
    }

    public void setData(Map<String, List> data) {
        this.data = data;
    }

    public Map<String, Object> getMapData() {
        return mapData;
    }

    public void setMapData(Map<String, Object> mapData) {
        this.mapData = mapData;
    }
}
