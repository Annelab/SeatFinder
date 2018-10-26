package com.example.loska.seatfinder;

public class Table {
    private int id;
    private int floor;
    private Facilities facilities;
    private String Type;
    private int noOfSeates;
    private int freeSeats;
    private String qrCode;

    public Table(int noOfSeates, String qrCode) {
        this.noOfSeates = noOfSeates;
        this.freeSeats = 0;
        this.qrCode = qrCode;

    }

    public int getNoOfSeates() {
        return noOfSeates;
    }

    public void setNoOfSeates(int noOfSeates) {
        this.noOfSeates = noOfSeates;
    }

    public int getFreeSeats() {
        return freeSeats;
    }

    public void setFreeSeats(int freeSeats) {
        this.freeSeats = freeSeats;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }


}
