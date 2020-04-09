package model;

import annotations.Entity;
import annotations.Name;
import lombok.Data;

import java.util.List;

@Data
@Name("Car Park")
@Entity
public class CarPark {
    @Name("Slot count")
    private long slotCount;
    @Name("Floor count")
    private long floorCount;
    @Name("Parked cars")
    private List<Transport> cars;

}
