package com.notepadone.blacklog.Objects;

public class TrucksObject {

    String signal;
    String basic;
    String FuelLid;
    String lastUpdate;
    String speed;
    String device_id;
    String ts;
    String vehicle_latitude;
    String vehicle_longitude;
    TrucksObject(){

    }
    public TrucksObject(String vehicle_longitude, String vehicle_latitude,String ts,String device_id, String signal, String basic, String FuelLid, String lastUpdate,String speed){
        this.ts = ts;
        this.device_id = device_id;
        this.signal = signal;
        this.basic = basic;
        this.FuelLid = FuelLid;
        this.speed = speed;
        this.lastUpdate = lastUpdate;
        this.vehicle_longitude = vehicle_longitude;
        this.vehicle_latitude = vehicle_latitude;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public void setVehicle_latitude(String vehicle_latitude) {
        this.vehicle_latitude = vehicle_latitude;
    }

    public void setVehicle_longitude(String vehicle_longitude) {
        this.vehicle_longitude = vehicle_longitude;
    }

    public String getVehicle_latitude() {
        return vehicle_latitude;
    }

    public String getVehicle_longitude() {
        return vehicle_longitude;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public String getBasic() {
        return basic;
    }

    public void setBasic(String basic) {
        this.basic = basic;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setFuelLid(String fuelLid) {
        FuelLid = fuelLid;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public String getSignal() {
        return signal;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getFuelLid() {
        return FuelLid;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
}
