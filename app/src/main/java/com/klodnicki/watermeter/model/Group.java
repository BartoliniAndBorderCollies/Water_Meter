package com.klodnicki.watermeter.model;

import com.klodnicki.watermeter.command.Command;

import java.util.Map;

public class Group {
    private String label;
    private Map<String, Command> items;


    public Map<String, Command> getItems() {
        return items;
    }

}
