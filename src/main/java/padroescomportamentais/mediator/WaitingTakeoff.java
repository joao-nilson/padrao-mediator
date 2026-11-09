package padroescomportamentais.mediator;

/** Waiting for the tower to clear the takeoff. No action is allowed. */
public class WaitingTakeoff implements FlightState {

    @Override
    public String getName() {
        return "WAITING_TAKEOFF";
    }
}
