package padroescomportamentais.mediator;

/** Mediator: the only way colleagues (aircraft) talk to the airport. */
public interface Tower {

    void requestLanding(Aircraft aircraft);

    void landed(Aircraft aircraft);

    void requestTakeoff(Aircraft aircraft);

    void departed(Aircraft aircraft);
}
