package com.example.dataselfie.RegistroSueno;

import java.util.Date;

public class DatosSueno{
    private Date date;
    private String sleepTime;
    private String wakeTime;
    private double hoursSlept;

    public DatosSueno() {

    }

    public DatosSueno(Date date, String sleepTime, String wakeTime, double hoursSlept) {
        this.date = date;
        this.sleepTime = sleepTime;
        this.wakeTime = wakeTime;
        this.hoursSlept = hoursSlept;
    }

    // Getters y Setters
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(String sleepTime) {
        this.sleepTime = sleepTime;
    }

    public String getWakeTime() {
        return wakeTime;
    }

    public void setWakeTime(String wakeTime) {
        this.wakeTime = wakeTime;
    }

    public double getHoursSlept() {
        return hoursSlept;
    }

    public void setHoursSlept(double hoursSlept) {
        this.hoursSlept = hoursSlept;
    }
}