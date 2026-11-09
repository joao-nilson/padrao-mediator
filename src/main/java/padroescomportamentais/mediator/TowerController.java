package padroescomportamentais.mediator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/** Concrete mediator: sequences landings and takeoffs using runways and gates. */
public class TowerController implements Tower {

    private final List<Runway> runways = new ArrayList<>();
    private final List<Gate> gates = new ArrayList<>();
    private final Deque<Aircraft> landingQueue = new ArrayDeque<>();
    private final Deque<Aircraft> takeoffQueue = new ArrayDeque<>();
    private final List<String> log = new ArrayList<>();

    public void addRunway(Runway runway) {
        runways.add(runway);
    }

    public void addGate(Gate gate) {
        gates.add(gate);
    }

    @Override
    public void requestLanding(Aircraft aircraft) {
        landingQueue.add(aircraft);
        log.add(aircraft + " requests landing");
        sequence();
    }

    @Override
    public void landed(Aircraft aircraft) {
        aircraft.getRunway().release();
        aircraft.setRunway(null);
        log.add(aircraft + " landed and parked at " + aircraft.getGate());
        sequence();
    }

    @Override
    public void requestTakeoff(Aircraft aircraft) {
        takeoffQueue.add(aircraft);
        log.add(aircraft + " requests takeoff");
        sequence();
    }

    @Override
    public void departed(Aircraft aircraft) {
        aircraft.getRunway().release();
        aircraft.setRunway(null);
        log.add(aircraft + " departed");
        sequence();
    }

    public int getHoldingCount() {
        return landingQueue.size();
    }

    public int getTakeoffQueueSize() {
        return takeoffQueue.size();
    }

    public List<String> getLog() {
        return Collections.unmodifiableList(log);
    }

    private void sequence() {
        // takeoffs first: they free gates that may unblock landings
        while (!takeoffQueue.isEmpty()) {
            Runway runway = freeRunway();
            if (runway == null) {
                break;
            }
            Aircraft aircraft = takeoffQueue.poll();
            aircraft.getGate().release();
            log.add(aircraft + " cleared for takeoff on " + runway);
            aircraft.setGate(null);
            runway.occupy(aircraft);
            aircraft.setRunway(runway);
            aircraft.setState(new TakingOff());
        }
        while (!landingQueue.isEmpty()) {
            Runway runway = freeRunway();
            Gate gate = freeGate();
            if (runway == null || gate == null) {
                break;
            }
            Aircraft aircraft = landingQueue.poll();
            runway.occupy(aircraft);
            gate.occupy(aircraft);
            aircraft.setRunway(runway);
            aircraft.setGate(gate);
            aircraft.setState(new Landing());
            log.add(aircraft + " cleared to land on " + runway + ", assigned " + gate);
        }
    }

    private Runway freeRunway() {
        for (Runway runway : runways) {
            if (runway.isFree()) {
                return runway;
            }
        }
        return null;
    }

    private Gate freeGate() {
        for (Gate gate : gates) {
            if (gate.isFree()) {
                return gate;
            }
        }
        return null;
    }
}
