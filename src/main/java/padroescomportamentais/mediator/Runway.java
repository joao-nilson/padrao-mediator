package padroescomportamentais.mediator;

/** Colleague: a runway that holds at most one aircraft at a time. */
public class Runway {

    private final String name;
    private Aircraft occupant;

    public Runway(String name) {
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
