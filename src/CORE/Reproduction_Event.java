package CORE;

import MODE.Grid;
import MODE.Individual;
import MODE.Coordenadas;

/**
 * Implements the {@link Event_Strategy} for a "Reproduction" event.
 * This strategy defines the behavior of an {@link Individual} reproducing,
 * which involves creating a new child individual, scheduling the parent's
 * next reproduction, and scheduling initial events for the newly created child.
 * The timing of these events is influenced by the individual's "comfort" level.
 */
public class Reproduction_Event implements Event_Strategy {

	/**
	 * Constructs a new Reproduction_Event.
	 * No specific setup is required for this event strategy at initialization.
	 */
	public Reproduction_Event() {
		// sem setup
	}

	/**
	 * Executes the reproduction logic for a parent individual within the simulation context.
	 * This involves:
	 * <ol>
	 * <li>Rescheduling the parent's next reproduction event.</li>
	 * <li>Creating a new child individual, inheriting part of the parent's path.</li>
	 * <li>Assigning birth and death times to the child and adding it to the population.</li>
	 * <li>Scheduling initial death, reproduction, and movement events for the child,
	 * provided they occur within the simulation's active time frame.</li>
	 * </ol>
	 * The timing of future events for both parent and child is influenced by their
	 * respective comfort levels.
	 *
	 * @param context The {@link Simulation_Context} providing access to the grid,
	 * event factory, PEC, population manager, and other simulation parameters.
	 * @param parent The {@link Individual} that is reproducing (the parent).
	 */
	@Override
	public void execute(Simulation_Context context, Individual parent) {
		// Retrieve necessary simulation parameters and components
		int now   = context.getTempoAtual();
		int death = parent.getDeathTime();    // Parent's scheduled death time
		int tau   = context.getTempoFinal();  // Simulation's end time
		EventFactory f = context.getEventFactory();
		Grid grid       = context.getGrid();
		Coordenadas tgt = context.getDestino();
		int k           = context.getK();     // Comfort weighting factor

		// 1) Reschedule the parent's next reproduction event
		// The duration until next reproduction is generated exponentially, influenced by parent's comfort (rho).
		int dParent = gerarExpo(context.getRho(), parent, context);
		int tParent = now + dParent;
		// Schedule only if the next reproduction occurs before the parent's death and before simulation end
		if (tParent < death && tParent <= tau) {
			context.getPEC().addEvent(f.createReproductionEvent(parent, tParent));
		}

		// 2) Create the child individual with a path prefix inherited from the parent.
		// The path inheritance logic is handled within the Individual.reproduz method (90% + phi * 10%).
		Individual child = parent.reproduz(k, grid, tgt);

		// 3) Assign birth time and death time to the child, then add to the population.
		child.setBirthTime(now); // Child is born at the current simulation time
		// Child's death time is also exponentially distributed, influenced by child's comfort (mu).
		int dChild    = gerarExpo(context.getMu(), child, context);
		int tDeath    = now + dChild;
		child.setDeathTime(tDeath);
		context.getPopulation().add(child); // Add the new child to the population manager

		// 4) Schedule initial events for the child, only if they fall within the simulation's timeline (tau)
		if (now <= tau) {
			// Schedule child's death event
			if (tDeath <= tau) {
				context.getPEC().addEvent(f.createDeathEvent(child, tDeath));
			}
			// Schedule child's first reproduction event
			int tRepr = now + gerarExpo(context.getRho(), child, context);
			// Only schedule if it's before child's death and within simulation end
			if (tRepr < tDeath && tRepr <= tau) {
				context.getPEC().addEvent(f.createReproductionEvent(child, tRepr));
			}
			// Schedule child's first movement event
			int tMove = now + gerarExpo(context.getDelta(), child, context);
			// Only schedule if it's before child's death and within simulation end
			if (tMove < tDeath && tMove <= tau) {
				context.getPEC().addEvent(f.createMoveEvent(child, tMove));
			}
		}
	}

	/**
	 * Generates a random integer from an exponential distribution, with its mean
	 * dynamically adjusted based on the individual's "comfort" level ($\phi$) and a base mean.
	 * The formula for the rate parameter $\lambda$ is $\lambda = \frac{1}{(1 - \ln(\phi)) \cdot \text{mediaBase}}$.
	 * The random number is derived using the inverse transform sampling method for exponential distribution.
	 *
	 * @param mediaBase The base mean value for the exponential distribution (e.g., $\rho$, $\mu$, $\delta$).
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