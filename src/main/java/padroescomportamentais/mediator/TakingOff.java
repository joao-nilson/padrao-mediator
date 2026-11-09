package padroescomportamentais.mediator;

public class TakingOff implements FlightState {

    @Override
    public void departed(Aircraft aircraft) {
        aircraft.setState(new Airborne());
        aircraft.getTower().departed(aircraft);
    }

    @Override
    public String getName() {
        return "TAKING_OFF";
    }
}
