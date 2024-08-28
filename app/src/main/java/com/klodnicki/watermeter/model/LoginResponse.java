package com.klodnicki.watermeter.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("Token")
    private String token;

    @SerializedName("TrackingToken")
    private String trackingToken;

    @SerializedName("type")
    private String type;

    @SerializedName("block")
    private boolean block;

    @SerializedName("msg")
    private String msg;

    @SerializedName("startTimePause")
    private String startTimePause;

    @SerializedName("endTimePause")
    private String endTimePause;

    @SerializedName("userid")
    private String userId;

    // Getters
    public String getToken() {
        return token;
    }

    public String getTrackingToken() {
        return trackingToken;
    }

    public String getType() {
        return type;
    }

    public boolean isBlock() {
        return block;
    }

    public String getMsg() {
        return msg;
    }

    public String getStartTimePause() {
        return startTimePause;
    }

    public String getEndTimePause() {
        return endTimePause;
    }

    public String getUserId() {
        return userId;
    }
}

