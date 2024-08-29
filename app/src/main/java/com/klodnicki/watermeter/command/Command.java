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

    public String getPayload() {
        return payload;
    }

    public Map<String, Parameter> getParameters() {
        return parameters;
    }
}
