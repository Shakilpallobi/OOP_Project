package CORE;

import java.util.List;
import MODE.Grid;
import MODE.Coordenadas;
import MODE.Individual;
import io.SimulationParameters; // Assuming SimulationParameters is in the 'io' package

/**
 * Represents the central context for the simulation, holding references
 * to all major components and shared simulation state.
 * It acts as a bridge between different parts of the simulation, allowing
 * them to access necessary data and services.
 */
public class Simulation_Context {
	/**
	 * The {@link Grid} representing the simulation environment.
	 */
	private Grid grid;
	/**
	 * The {@link PEC} (Priority Event Calendar) managing scheduled events.
	 */
	private PEC pec;
	/**
	 * The {@link PopulationManager} overseeing the population of individuals.
	 */
	private PopulationManager population;
	/**
	 * The {@link SimulationParameters} containing the initial configuration of the simulation.
	 */
	private SimulationParameters parameters;
	/**
	 * An {@link EventFactory} used to create various types of simulation events.
	 */
	private EventFactory eventFactory;
	/**
	 * The current simulated time. This value is updated as events are processed.
	 */
	private int tempoAtual;

	/**
	 * Constructs a new Simulation_Context.
	 *
	 * @param grid The {@link Grid} instance for the simulation.
	 * @param pec The {@link PEC} instance for managing events.
	 * @param population The {@link PopulationManager} instance for managing individuals.
	 * @param parameters The {@link SimulationParameters} for the simulation.
	 */
	public Simulation_Context(Grid grid, PEC pec, PopulationManager population, SimulationParameters parameters) {
		this.grid = grid;
		this.pec = pec;
		this.population = population;
		this.parameters = parameters;
		this.eventFactory = new EventFactory(); // Initialize EventFactory within the context
		this.tempoAtual = 0; // Simulation starts at time 0
	}

	/**
	 * Returns the {@link Grid} object associated with this simulation context.
	 * @return The simulation grid.
	 */
	public Grid getGrid() { return grid; }
	/**
	 * Returns the {@link PEC} (Priority Event Calendar) object associated with this simulation context.
	 * @return The event calendar.
	 */
	public PEC getPEC() { return pec; }
	/**
	 * Returns the {@link PopulationManager} object associated with this simulation context.
	 * @return The population manager.
	 */
	public PopulationManager getPopulation() { return population; }
	/**
	 * Returns the {@link SimulationParameters} object containing the initial configuration.
	 * @return The simulation parameters.
	 */
	public SimulationParameters getParameters() { return parameters; }
	/**
	 * Returns the {@link EventFactory} object used to create events in this simulation.
	 * @return The event factory.
	 */
	public EventFactory getEventFactory() { return eventFactory; }
	/**
	 * Returns the current simulated time.
	 * @return The current time.
	 */
	public int getTempoAtual() { return tempoAtual; }
	/**
	 * Sets the current simulated time. This method is typically called by the
	 * simulation loop to advance time as events are processed.
	 * @param tempo The new current time.
	 */
	public void setTempoAtual(int tempo) { this.tempoAtual = tempo; }

	/**
	 * Returns the final destination point (target coordinates) for individuals in the simulation.
	 * This is retrieved from the {@link SimulationParameters}.
	 * @return The {@link Coordenadas} of the destination.
	 */
	public Coordenadas getDestino() { return parameters.getEndPoint(); }
	/**
	 * Returns the 'k' parameter, a weighting factor used in comfort calculation.
	 * This is retrieved from the {@link SimulationParameters}.
	 * @return The value of k.
	 */
	public int getK() { return parameters.getK(); }
	/**
	 * Returns the number of rows (N) in the grid.
	 * This is retrieved from the {@link SimulationParameters}.
	 * @return The number of grid rows.
	 */
	public int getN() { return parameters.getN(); }
	/**
	 * Returns the number of columns (M) in the grid.
	 * This is retrieved from the {@link SimulationParameters}.
	 * @return The number of grid columns.
	 */
	public int getM() { return parameters.getM(); }
	/**
	 * Returns the total simulation time (tau).
	 * This is retrieved from the {@link SimulationParameters}.
	 * @return The maximum simulation time.
	 */
	public int getTempoFinal() { return parameters.getTau(); }
	/**
	 * Returns the mean time for death events (mu).
	 * This is retrieved from the {@link SimulationParameters}.
	 * @return The mu parameter.
	 */
	public double getMu() { return parameters.getMu(); }
	/**
	 * Returns the mean time for reproduction events (rho).
	 * This is retrieved from the {@link SimulationParameters}.
	 * @return The rho parameter.
	 */
	public double getRho() { return parameters.getRho(); }
	/**
	 * Returns the mean time for movement events (delta).
	 * This is retrieved from the {@link SimulationParameters}.
	 * @return The delta parameter.
	 */
	public double getDelta() { return parameters.getDelta(); }

	/**
	 * Identifies and returns the "best fit" individual among all individuals
	 * currently in the population. The criteria for "best fit" are:
	 * <ol>
	 * <li>Priority 1: Any individual that has reached the target destination,
	 * with the one having the lowest path cost being preferred.</li>
	 * <li>Priority 2: If no individual has reached the target, then the individual
	 * with the highest "comfort" level among all individuals.</li>
	 * </ol>
	 *
	 * @return The {@link Individual} considered the best fit, or {@code null} if the population is empty.
	 */
	public Individual getBestFitIndividual() {
		Coordenadas destino = getDestino();
		List<Individual> all = population.getALL();

		// Priority 1: Find the individual that reached the destination with the lowest cost
		Individual melhorComDestino = null;
		int melhorCusto = Integer.MAX_VALUE;
		for (Individual i : all) {
			if (i.getLastPosition().equals(destino)) {
				int custo = i.getCost(grid);
				if (custo < melhorCusto) {
					melhorCusto = custo;
					melhorComDestino = i;
				}
			}
		}
		// If an individual that reached the destination is found, return it
		if (melhorComDestino != null) return melhorComDestino;

		// Priority 2: If no individual reached the destination, return the one with the highest comfort
		// Uses Java 8 Streams for concise maximum finding based on comfort
		return all.stream()
				.max((a, b) -> Double.compare(
						a.getComfort(grid, destino, getK()), // Compare comfort of individual 'a'
						b.getComfort(grid, destino, getK()))) // against individual 'b'
				.orElse(null); // Return null if the population list is empty
	}
}