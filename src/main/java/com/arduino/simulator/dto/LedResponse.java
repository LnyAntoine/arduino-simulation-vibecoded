package com.arduino.simulator.dto;

public class LedResponse {
    private int status;
    private int pos;

    public LedResponse(int status, int pos) {
        this.status = status;
        this.pos = pos;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
