package padroescomportamentais.mediator;

/**
 * State: what an aircraft can do depends on its current state.
 * By default every action is invalid; each concrete state overrides only what it allows.
 */
public interface FlightState {

    default void requestLanding(Aircraft aircraft) {
        throw invalid(aircraft, "request landing");
    }

    default void landed(Aircraft aircraft) {
        throw invalid(aircraft, "land");
    }

    default void requestTakeoff(Aircraft aircraft) {
        throw invalid(aircraft, "request takeoff");
    }

    default void departed(Aircraft aircraft) {
        throw invalid(aircraft, "depart");
    }

    String getName();

    default IllegalStateException invalid(Aircraft aircraft, String action) {
        return new IllegalStateException(aircraft + " is " + getName() + " and cannot " + action);
    }
}
