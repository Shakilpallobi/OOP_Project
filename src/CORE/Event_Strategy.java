package CORE;

import MODE.Individual;
/**
 * Defines the strategy interface for handling various events within the simulation.
 * Classes implementing this interface provide a specific implementation for an event's
 * execution, allowing for polymorphic behavior based on the type of event.
 * <p>
 * This interface is a key part of the Strategy design pattern, enabling different
 * event behaviors to be encapsulated and interchanged.
 */
public interface Event_Strategy {
    /**
     * Executes the specific logic associated with this event strategy.
     * This method is responsible for performing the actions relevant to the event,
     * often interacting with the simulation context and possibly an individual participant.
     *
     * @param aContext The simulation context, providing access to shared simulation state,
     * such as the grid, obstacle information, or other global parameters.
     * @param aIndividual The individual (e.g., an agent, a moving entity) on which
     * this event might act or whose state might be affected by the event.
     * Can be {@code null} if the event is not specific to an individual.
     */
	void execute(Simulation_Context aContext, Individual aIndividual);
}
