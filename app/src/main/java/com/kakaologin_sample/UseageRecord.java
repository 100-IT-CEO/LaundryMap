package com.kakaologin_sample;

public class UseageRecord {
    String date;
    String location;
    String machine_type;
    String operation_time;

    public UseageRecord(String date, String location, String machine_type, String operation_time) {
        this.date = date;
        this.location = location;
        this.machine_type = machine_type;
        this.operation_time = operation_time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMachine_type() {
        return machine_type;
    }

    public void setMachine_type(String machine_type) {
        this.machine_type = machine_type;
    }

    public String getOperation_time() {
        return operation_time;
    }

    public void setOperation_time(String operation_time) {
        this.operation_time = operation_time;
    }
}
