package model;

import annotations.Entity;
import annotations.Name;

import java.sql.Time;
import java.util.Map;

@Name("City transport")
@Entity
public class CityTransport extends PublicTransport {
    @Name("Schedule")
    private Map<BusStop, Time> schedule;
    @Name("Next bus stop")
    private BusStop nextStop;
    @Name("Has conductor")
    private boolean presentConductor;

    public Map<BusStop, Time> getSchedule() {
        return schedule;
    }

    public void setSchedule(Map<BusStop, Time> schedule) {
        this.schedule = schedule;
    }

    public boolean isPresentConductor() {
        return presentConductor;
    }

    public void setPresentConductor(boolean presentConductor) {
        this.presentConductor = presentConductor;
    }

    @Override
    public void rideTo(String destAddress) {

    }
}
