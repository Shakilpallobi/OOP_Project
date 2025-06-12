package CORE;

import MODE.Individual; // Assuming MODE.Individual is the correct path for the Individual class

/**
 * A factory class for creating different types of {@link Event} objects.
 * This class follows the Factory Method design pattern, providing a centralized
 * way to instantiate various event types with their corresponding {@link Event_Strategy}.
 * It helps in decoupling the client code from the concrete implementations of event strategies.
 */
public class EventFactory {

    /**
     * Constructs a new EventFactory.
     * In this specific case, no special initialization is required for the factory.
     */
    public EventFactory() {
        // Nenhuma inicialização necessária neste caso
    }

    /**
     * Creates a new {@link Event} representing a movement action.
     * The event will be scheduled for the specified time and will execute
     * a {@link Move_Event} strategy.
     *
     * @param individual The {@link Individual} that will perform the move action.
     * @param time The simulated time at which this move event should occur.
     * @return A new {@link Event} object configured for a movement.
     */
    public Event createMoveEvent(Individual individual, int time) {
        return new Event(time, individual, new Move_Event());
    }

    /**
     * Creates a new {@link Event} representing a death occurrence.
     * The event will be scheduled for the specified time and will execute
     * a {@link Death_Event} strategy.
     *
     * @param individual The {@link Individual} associated with the death event.
     * This is typically the individual that "dies".
     * @param time The simulated time at which this death event should occur.
     * @return A new {@link Event} object configured for a death event.
     */
    public Event createDeathEvent(Individual individual, int time) {
        return new Event(time, individual, new Death_Event());
    }

    /**
     * Creates a new {@link Event} representing a reproduction action.
     * The event will be scheduled for the specified time and will execute
     * a {@link Reproduction_Event} strategy.
     *
     * @param individual The {@link Individual} initiating or involved in the reproduction event.
     * @param time The simulated time at which this reproduction event should occur.
     * @return A new {@link Event} object configured for a reproduction event.
     */
    public Event createReproductionEvent(Individual individual, int time) {
        return new Event(time, individual, new Reproduction_Event());
    }
}