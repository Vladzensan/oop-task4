package model;

import annotations.Entity;
import annotations.Name;
import lombok.Data;

import java.sql.Time;
import java.util.Map;

@Name("City transport")
@Entity
@Data
public class CityTransport extends PublicTransport {
    @Name("Schedule")
    private Map<BusStop, Time> schedule;
    @Name("Next bus stop")
    private BusStop nextStop;
    @Name("Has conductor")
    private boolean presentConductor;


    @Override
    public void rideTo(String destAddress) {

    }
}
