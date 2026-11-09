package padroescomportamentais.mediator;

public class AtGate implements FlightState {

    @Override
    public void requestTakeoff(Aircraft aircraft) {
        aircraft.setState(new WaitingTakeoff());
        aircraft.getTower().requestTakeoff(aircraft);
    }

    @Override
    public String getName() {
        return "AT_GATE";
    }
}
