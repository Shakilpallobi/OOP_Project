package CORE;

import MODE.Grid;
import MODE.Individual;
import MODE.Coordenadas;

import java.util.List;

/**
 * Implements the {@link Event_Strategy} for a "Move" event.
 * This strategy defines the behavior of an {@link Individual} moving on the {@link Grid}.
 * When executed, it attempts to move the individual to a valid adjacent position,
 * updates its path, and schedules its next movement event.
 * The timing of the next movement is influenced by the individual's "comfort" level.
 */
public class Move_Event implements Event_Strategy {

	/**
	 * Constructs a new Move_Event.
	 * No specific setup is required for this event strategy at initialization.
	 */
	public Move_Event() {
		// sem setup
	}

	/**
	 * Executes the movement logic for an individual within the simulation context.
	 * This involves:
	 * <ol>
	 * <li>Determining valid adjacent moves for the individual.</li>
	 * <li>If valid moves exist, randomly selecting one.</li>
	 * <li>Moving the individual to the selected position and optimizing its path.</li>
	 * <li>Scheduling the individual's next move event based on a dynamically
	 * calculated time influenced by its comfort level.</li>
	 * </ol>
	 *
	 * @param context The {@link Simulation_Context} providing access to the grid,
	 * event factory, PEC, and other simulation parameters.
	 * @param individual The {@link Individual} that is performing the move.
	 */
	@Override
	public void execute(Simulation_Context context, Individual individual) {

		// Retrieve necessary components from the context
		Grid grid = context.getGrid();
		Coordenadas current = individual.getCurrentPosition();

		// Get all valid (non-obstacle, within-bounds) adjacent positions
		List<Coordenadas> valid = grid.getValidMoves(current);

		// Proceed only if there are valid moves available
		if (!valid.isEmpty()) {
			// Choose a random valid adjacent coordinate to move to
			int idx = (int) Math.floor(Math.random() * valid.size());
			Coordenadas next = valid.get(idx);

			// Update the individual's position and remove any cycles from its path
			individual.moveTo(next);

			// Schedule the next movement event for this individual
			int now = context.getTempoAtual();      // Current simulation time
			int death = individual.getDeathTime();  // Individual's scheduled death time
			int tau = context.getTempoFinal();      // Simulation's end time

			// Calculate the duration until the next move using an exponential distribution,
			// with the mean influenced by the individual's comfort.
			int delta = gerarExpo(context.getDelta(), individual, context);
			int nextTime = now + delta; // Calculate the absolute time of the next event

			// Only schedule the next move if it occurs before the individual's death time
			// and before the overall simulation end time.
			if (nextTime < death && nextTime <= tau) {
				Event me = context.getEventFactory().createMoveEvent(individual, nextTime);
				context.getPEC().addEvent(me);
			}
		}
	}

	/**
	 * Generates a random integer from an exponential distribution.
	 * The mean of this distribution is dynamically adjusted based on the individual's
	 * comfort ($\phi$) and a base mean value. The formula for lambda (rate parameter) is:
	 * $\lambda = \frac{1}{(1 - \ln(\phi)) \cdot \text{mediaBase}}$.
	 * The random number is then generated using the inverse transform sampling method for exponential distribution.
	 *
	 * @param mediaBase The base mean value for the exponential distribution (parameter $\delta$).
	 * @param ind The {@link Individual} whose comfort level ($\phi$) influences the distribution's mean.
	 * @param ctx The {@link Simulation_Context} to access the grid, target, and K for comfort calculation.
	 * @return A randomly generated integer representing a time duration, rounded up to the nearest integer.
	 */
	private int gerarExpo(double mediaBase, Individual ind, Simulation_Context ctx) {
		double phi = ind.getComfort(ctx.getGrid(), ctx.getDestino(), ctx.getK());
		// Calculate lambda (rate parameter) based on phi and mediaBase
		// Note: Math.log is natural logarithm (ln)
		double lambda = 1.0 / ((1 - Math.log(phi)) * mediaBase);
		double u = Math.random(); // Uniform random number [0, 1)
		// Inverse transform sampling for exponential distribution
		return (int) Math.ceil(-Math.log(1 - u) / lambda);
	}
}