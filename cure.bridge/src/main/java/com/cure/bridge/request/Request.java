package com.cure.bridge.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class Request {
    private String text;
    private String flag;
    private Object[] symptoms;
    private Object[] selected_indices;
    private String action;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Object[] getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(Object[] symptoms) {
        this.symptoms = symptoms;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Object[] getSelected_indices() {
        return selected_indices;
    }

    public void setSelected_indices(Object[] selected_indices) {
        this.selected_indices = selected_indices;
    }
}
