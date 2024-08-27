package com.klodnicki.watermeter.model;

import com.klodnicki.watermeter.command.Command;

import java.util.Map;

public class Group {
    private String label;
    private Map<String, Command> items;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Map<String, Command> getItems() {
        return items;
    }

    public void setItems(Map<String, Command> items) {
        this.items = items;
    }
}
