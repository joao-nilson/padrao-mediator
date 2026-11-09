package padroescomportamentais.mediator;

public class Landing implements FlightState {

    @Override
    public void landed(Aircraft aircraft) {
        aircraft.setState(new AtGate());
        aircraft.getTower().landed(aircraft);
    }

    @Override
    public String getName() {
        return "LANDING";
    }
}
