package padroescomportamentais.mediator;

public class Airborne implements FlightState {

    @Override
    public void requestLanding(Aircraft aircraft) {
        aircraft.setState(new Holding());
        aircraft.getTower().requestLanding(aircraft);
    }

    @Override
    public String getName() {
        return "AIRBORNE";
    }
}
