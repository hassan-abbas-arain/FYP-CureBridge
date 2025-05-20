package com.cure.bridge.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DPRequest {
    private Object[] selected_indices;
    private String action;

    public Object[] getSelected_indices() {
        return selected_indices;
    }

    public void setSelected_indices(Object[] selected_indices) {
        this.selected_indices = selected_indices;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
