package com.notepadone.blacklog.Objects;

public class TrucksObject {

    String signal;
    String basic;
    String FuelLid;
    String lastUpdate;
    String speed;
    String device_id;
    TrucksObject(){

    }
    public TrucksObject(String device_id, String signal, String basic, String FuelLid, String lastUpdate,String speed){
        this.device_id = device_id;
        this.signal = signal;
        this.basic = basic;
        this.FuelLid = FuelLid;
        this.speed = speed;
        this.lastUpdate = lastUpdate;
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
