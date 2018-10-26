package com.example.loska.seatfinder;

public class Facilities {
    private Boolean chargingSocket;
    private Boolean nearWindow;

    public Facilities(Boolean chargingSocket, Boolean nearWindow, Boolean eatingIsAllowed, Boolean accessibility) {
        this.chargingSocket = chargingSocket;
        this.nearWindow = nearWindow;
        this.eatingIsAllowed = eatingIsAllowed;
        this.accessibility = accessibility;
    }

    private Boolean eatingIsAllowed;
    private Boolean accessibility;

    public Boolean getChargingSocket() {
        return chargingSocket;
    }

    public Boolean getNearWindow() {
        return nearWindow;
    }

    public Boolean getEatingIsAllowed() {
        return eatingIsAllowed;
    }

    public Boolean getAccessibility() {
        return accessibility;
    }
}
