package com.klodnicki.watermeter.model;

import java.util.HashMap;
import java.util.Map;

public class PermissionsResponse {
    private Map<String, Group> groups = new HashMap<>();

    public Map<String, Group> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, Group> groups) {
        this.groups = groups;
    }
}

