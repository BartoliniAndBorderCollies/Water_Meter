package com.klodnicki.watermeter.command;

import com.klodnicki.watermeter.model.Parameter;

import java.util.Map;

public class Command {
    private String label;
    private String payload;
    private Map<String, Parameter> parameters;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Map<String, Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Parameter> parameters) {
        this.parameters = parameters;
    }
}
