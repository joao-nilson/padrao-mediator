package padroescomportamentais.mediator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TowerControllerTest {

    private TowerController tower;
    private Runway runway;
    private Gate gate;

    @BeforeEach
    void setUp() {
        tower = new TowerController();
        runway = new Runway("RWY-1");
        gate = new Gate("G1");
        tower.addRunway(runway);
        tower.addGate(gate);
    }

    @Test
    void shouldClearLandingWhenRunwayAndGateAreFree() {
        Aircraft a = new Aircraft("AA100", tower);
        a.requestLanding();

        assertTrue(a.getState() instanceof Landing);
        assertSame(runway, a.getRunway());
        assertSame(gate, a.getGate());
        assertFalse(runway.isFree());
        assertFalse(gate.isFree());
    }

    @Test
    void shouldFreeRunwayAndParkAtGateAfterLanding() {
        Aircraft a = new Aircraft("AA100", tower);
        a.requestLanding();
        a.landed();

        assertTrue(a.getState() instanceof AtGate);
        assertNull(a.getRunway());
        assertTrue(runway.isFree());
        assertSame(a, gate.getOccupant());
    }

    @Test
    void shouldHoldSecondAircraftUntilRunwayIsFree() {
        Aircraft a = new Aircraft("AA100", tower);
        Aircraft b = new Aircraft("BB200", tower);
        tower.addGate(new Gate("G2"));

        a.requestLanding();
        b.requestLanding();

        assertTrue(b.getState() instanceof Holding);
        assertEquals(1, tower.getHoldingCount());

        a.landed();

        assertTrue(b.getState() instanceof Landing);
        assertEquals(0, tower.getHoldingCount());
    }

    @Test
    void shouldHoldLandingWhenNoGateIsAvailable() {
        Aircraft a = new Aircraft("AA100", tower);
        Aircraft b = new Aircraft("BB200", tower);
        tower.addRunway(new Runway("RWY-2"));

        a.requestLanding();
        a.landed();
        b.requestLanding();

        assertTrue(b.getState() instanceof Holding);

        a.requestTakeoff();
        a.departed();

        assertTrue(b.getState() instanceof Landing);
        assertSame(gate, b.getGate());
    }

    @Test
    void shouldClearTakeoffAndFreeGate() {
        Aircraft a = new Aircraft("AA100", tower);
        a.requestLanding();
        a.landed();
        a.requestTakeoff();

        assertTrue(a.getState() instanceof TakingOff);
        assertSame(runway, a.getRunway());
        assertNull(a.getGate());
        assertTrue(gate.isFree());

        a.departed();

        assertTrue(a.getState() instanceof Airborne);
        assertTrue(runway.isFree());
    }

    @Test
    void shouldQueueTakeoffWhileRunwayIsBusy() {
        Aircraft a = new Aircraft("AA100", tower);
        Aircraft b = new Aircraft("BB200", tower);
        tower.addGate(new Gate("G2"));

        a.requestLanding();
        a.landed();
        b.requestLanding();       // takes the runway
        a.requestTakeoff();       // must wait

        assertTrue(a.getState() instanceof WaitingTakeoff);
        assertEquals(1, tower.getTakeoffQueueSize());

        b.landed();

        assertTrue(a.getState() instanceof TakingOff);
        assertEquals(0, tower.getTakeoffQueueSize());
    }

    @Test
    void shouldServeLandingsInArrivalOrder() {
        Aircraft a = new Aircraft("AA100", tower);
        Aircraft b = new Aircraft("BB200", tower);
        Aircraft c = new Aircraft("CC300", tower);
        tower.addGate(new Gate("G2"));
        tower.addGate(new Gate("G3"));

        a.requestLanding();
        b.requestLanding();
        c.requestLanding();
        a.landed();

        assertTrue(b.getState() instanceof Landing);
        assertTrue(c.getState() instanceof Holding);
    }

    @Test
    void shouldRejectInvalidTransitions() {
        Aircraft a = new Aircraft("AA100", tower);

        assertThrows(IllegalStateException.class, a::landed);
        assertThrows(IllegalStateException.class, a::requestTakeoff);
        assertThrows(IllegalStateException.class, a::departed);

        a.requestLanding();
        assertThrows(IllegalStateException.class, a::requestLanding);
    }

    @Test
    void shouldRecordCommunicationInTowerLog() {
        Aircraft a = new Aircraft("AA100", tower);
        a.requestLanding();

        assertEquals("AA100 requests landing", tower.getLog().get(0));
        assertEquals("AA100 cleared to land on RWY-1, assigned G1", tower.getLog().get(1));
    }
}
