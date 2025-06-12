package CORE;
import MODE.*;
/**
 * Represents a "Death" event within the simulation.
 * This class implements the {@link Event_Strategy} interface,
 * signifying that it defines a specific behavior or action to be performed
 * when a death event occurs.
 * <p>
 * In the context of the pathfinding problem, a "death" event might signify
 * reaching an obstacle or an invalid state, but without further context,
 * it broadly indicates an event that could lead to the termination
 * of a path or an entity's existence.
 */
public class Death_Event implements Event_Strategy {
    /**
     * Constructs a new Death_Event.
     * This constructor initializes the event. Specific logic related to
     * a death event's setup would be placed here if necessary.
     */

	public Death_Event() {
		// No setup needed
	}

	@Override
	public void execute(Simulation_Context context, Individual individual) {
		context.getPopulation().remove(individual);
	}
}