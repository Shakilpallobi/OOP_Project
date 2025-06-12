package CORE;

import MODE.Individual;
import MODE.Grid;
import MODE.Coordenadas;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages the population of {@link Individual} entities within the simulation.
 * This class is responsible for adding, removing, and querying individuals,
 * as well as implementing population-wide events like "epidemics" which affect
 * the survival of individuals based on their "comfort" level.
 */
public class PopulationManager {
	/**
	 * A list of all {@link Individual} entities currently tracked by the manager,
	 * including both living and dead individuals.
	 */
	private List<Individual> individuals = new ArrayList<>();
	/**
	 * A reference to the {@link Simulation_Context}, providing access to global
	 * simulation parameters, the grid, current time, and other shared resources.
	 */
	private Simulation_Context context;

	/**
	 * Constructs a new PopulationManager.
	 *
	 * @param context The {@link Simulation_Context} associated with this population manager.
	 * It can be {@code null} initially if a circular dependency exists (e.g., if
	 * the context needs a PopulationManager and vice-versa), but should be set later.
	 */
	public PopulationManager(Simulation_Context context) {
		this.context = context;
	}

	/**
	 * Adds a new individual to the population.
	 *
	 * @param individual The {@link Individual} to be added.
	 */
	public void add(Individual individual) {
		individuals.add(individual);
	}

	/**
	 * Removes a specified individual from the population.
	 *
	 * @param individual The {@link Individual} to be removed.
	 */
	public void remove(Individual individual) {
		individuals.remove(individual);
	}

	/**
	 * Returns a new list containing all individuals (both alive and potentially dead)
	 * currently managed by this PopulationManager.
	 *
	 * @return A {@link List} of all {@link Individual} objects.
	 */
	public List<Individual> getALL() {
		return new ArrayList<>(individuals);
	}

	/**
	 * Returns a list of individuals that are currently considered "alive" in the simulation.
	 * An individual is considered alive if its scheduled death time is later than the current
	 * simulation time.
	 *
	 * @return A {@link List} of alive {@link Individual} objects.
	 */
	public List<Individual> getALIVE() {
		int tempoAtual = context.getTempoAtual();
		return individuals.stream()
				.filter(i -> i.getDeathTime() > tempoAtual) // Filter individuals whose death time is in the future
				.collect(Collectors.toList());
	}

	/**
	 * Retrieves the top 'k' individuals from the currently alive population,
	 * ranked by their "comfort" level in descending order (higher comfort is better).
	 *
	 * @param k The number of top individuals to retrieve.
	 * @return A {@link List} containing the top 'k' alive {@link Individual} objects
	 * based on their comfort, or fewer if the total alive population is less than 'k'.
	 */
	public List<Individual> getTopK(int k) {
		return getALIVE().stream()
				.sorted(Comparator.comparingDouble(
						// Sort in descending order of comfort
						i -> -i.getComfort(context.getGrid(), context.getDestino(), context.getK())
				))
				.limit(k) // Take only the top 'k'
				.collect(Collectors.toList());
	}

	/**
	 * Simulates an "epidemic" event that affects the living population.
	 * In this event, the top 5 most "comfortable" individuals are guaranteed to survive.
	 * For all other individuals, their survival is determined by a random chance:
	 * an individual survives if a randomly generated number (between 0 and 1)
	 * is less than or equal to their "comfort" level. Individuals that do not survive
	 * are removed from the population.
	 * The epidemic only occurs if there are more than 5 living individuals.
	 */
	public void applyEpidemic() {
		List<Individual> vivos = getALIVE();
		if (vivos.size() <= 5) return; // Nothing to do if population is too small

		// Identify the top 5 most comfortable individuals who are guaranteed survivors
		List<Individual> top5 = getTopK(5);
		Set<Individual> sobreviventes = new HashSet<>(top5); // Use a Set for efficient lookup

		// Iterate through all alive individuals to determine survival
		for (Individual ind : new ArrayList<>(vivos)) { // Iterate over a copy to avoid ConcurrentModificationException during removal
			// If already marked as a survivor (e.g., part of top 5), skip
			if (sobreviventes.contains(ind)) continue;

			// Calculate comfort and generate a random number for survival chance
			double comfort = ind.getComfort(context.getGrid(), context.getDestino(), context.getK());
			double sorte = Math.random(); // Random number between [0.0, 1.0)

			// If random chance is higher than comfort, the individual "dies" (is removed)
			if (sorte <= comfort) {
				// This individual survives, add to the set of survivors (though technically already alive)
				// Adding here ensures they are retained if we were building a new list of alive individuals
				sobreviventes.add(ind);
			} else {
				// This individual does not survive the epidemic
				remove(ind);
			}
		}
	}
}