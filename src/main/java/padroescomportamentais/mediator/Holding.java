package padroescomportamentais.mediator;

/** Waiting for the tower to clear the landing. No action is allowed. */
public class Holding implements FlightState {

    @Override
    public String getName() {
        return "HOLDING";
    }
}
