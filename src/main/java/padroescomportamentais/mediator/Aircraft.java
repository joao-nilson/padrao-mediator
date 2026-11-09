package padroescomportamentais.mediator;

/**
 * Colleague (talks only to the tower) and State context
 * (delegates its actions to the current state).
 */
public class Aircraft {

    private final String callsign;
    private final Tower tower;
    private FlightState state = new Airborne();
    private Runway runway;
    private Gate gate;

    public Aircraft(String callsign, Tower tower) {
        this.callsign = callsign;
        this.tower = tower;
    }

    public void requestLanding() {
        state.requestLanding(this);
    }

    public void landed() {
        state.landed(this);
    }

    public void requestTakeoff() {
        state.requestTakeoff(this);
    }

    public void departed() {
        state.departed(this);
    }

    public String getCallsign() {
        return callsign;
    }

    public FlightState getState() {
        return state;
    }

    public Runway getRunway() {
        return runway;
    }

    public Gate getGate() {
        return gate;
    }

    Tower getTower() {
        return tower;
    }

    void setState(FlightState state) {
        this.state = state;
    }

    void setRunway(Runway runway) {
        this.runway = runway;
    }

    void setGate(Gate gate) {
        this.gate = gate;
    }

    @Override
    public String toString() {
        return callsign;
    }
}
