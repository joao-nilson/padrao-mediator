package padroescomportamentais.mediator;

/** Colleague: a gate that holds at most one aircraft at a time. */
public class Gate {

    private final String name;
    private Aircraft occupant;

    public Gate(String name) {
        this.name = name;
    }

    public boolean isFree() {
        return occupant == null;
    }

    void occupy(Aircraft aircraft) {
        this.occupant = aircraft;
    }

    void release() {
        this.occupant = null;
    }

    public Aircraft getOccupant() {
        return occupant;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
