package com.example.loska.seatfinder;

public class Booking {
    private Person person;
    private Table table;
    private double bookingTime;
    public Booking(Person person, Table table, double bookingTime) {
        this.person = person;
        this.table = table;
        this.bookingTime = bookingTime;
    }



    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public double getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(double bookingTime) {
        this.bookingTime = bookingTime;
    }
}
