package model;

import annotations.Entity;
import annotations.Name;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
public abstract class Transport  implements Serializable {
    @Name("Model")
    private String model;
    @Name("Reg. number")
    private String registrationNumber;
    @Name("Max speed")
    private int maxSpeed;
    @Name("Engine capacity")
    private float engineCapacity;
    @Name("Seat count")
    private long seatCount;
    @Name("Current location")
    private String currentLocation;
    @Name("Transport type")
    private TransportType transportType;

}


/*
 public String getModel() {
        return model;
    }

    + setModel(String model)
    + getMaxSpeed(): int
    + setMaxSpeed(int maxSpeed)
    + getEngineCapacity(): float
    + setEngineCapacity(float engineCapacity)
    + getSeatCount(): long
    + setSeatCount(long seatCount)
    + getCurrentLocation(): String
    + setCurrentLocation(String currentLocation)
    + getRegistrationNumber(): String
    + setRegistrationNumber(String registrationNumber)
 */