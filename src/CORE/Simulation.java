package CORE;

import MODE.Grid;
import MODE.Individual;
import MODE.Coordenadas;
import io.SimulationParameters;
import MODE.Special_Cost_Zone;
import java.util.List;
import java.util.Comparator; // Added import for Comparator

/**
 * Manages and executes the simulation process.
 * This class orchestrates the event-driven simulation loop, processes events
 * from the {@link PEC}, updates the simulation state, and provides
 * periodic observations and final results.
 */
public class Simulation {
	/**
	 * The central {@link Simulation_Context} holding all shared simulation data,
	 * parameters, and references to other core components.
	 */
	private final Simulation_Context context;

	/**
	 * Constructs a new Simulation instance with the given simulation context.
	 *
	 * @param context The {@link Simulation_Context} that defines the simulation environment.
	 */
	public Simulation(Simulation_Context context) {
		this.context = context;
	}

	/**
	 * Runs the main simulation loop.
	 * This method performs the following steps:
	 * <ol>
	 * <li>Prints initial simulation parameters.</li>
	 * <li>Enters a loop to process events from the {@link PEC} in chronological order.</li>
	 * <li>Periodically prints observations (every {@code tau/20} time units).</li>
	 * <li>Executes each event and updates the simulation time.</li>
	 * <li>Continues until no more events are left or the simulation time exceeds {@code tau}.</li>
	 * <li>Prints any remaining observations to reach 20 total observations.</li>
	 * <li>Identifies and prints the "best fit" individual at the end of the simulation.</li>
	 * </ol>
	 */
	public void run() {
		// 1) Print initial parameters (input echo)
		printInputParameters(context.getParameters());

		// Retrieve key simulation parameters
		int tau = context.getParameters().getTau(); // Total simulation time
		// Calculate the interval for observations (20 observations over tau)
		int obsInterval = tau / 20;
		int nextObsTime = 0; // Time for the next observation
		int obsNum = 0;      // Current observation number (0 to 20)
		int events = 0;      // Counter for realized events
		int currentTime = 0; // Current simulation time, updated by events

		// 2) Main event execution loop
		// Continues as long as there are events in the PEC
		while (context.getPEC().hasEvents()) {
			Event event = context.getPEC().getNextEvent();
			if (event == null) break; // Should not happen if hasEvents() is true, but as a safeguard

			currentTime = event.getTime(); // Advance simulation time to the current event's time

			// **Inject current time into the simulation context**
			context.setTempoAtual(currentTime);

			// Print observations at scheduled intervals (0 to 20 total)
			while (currentTime >= nextObsTime && obsNum <= 20) {
				Individual best = findBestIndividual(); // Find the best individual for current observation
				// Check if the best individual has reached the target destination
				boolean hit = best != null && best.getLastPosition().equals(context.getDestino());
				// Print the current observation's details
				printObservation(obsNum, nextObsTime, events,
						context.getPopulation().getALL().size(), // Total population size (alive + dead)
						hit, best);
				obsNum++; // Increment observation number
				nextObsTime = obsNum * obsInterval; // Calculate time for the next observation
			}

			// Execute the current event and increment the event counter
			event.execute(context);
			events++;

			// Terminate simulation if the current time exceeds the total simulation time (tau)
			if (currentTime > tau) break;
		}

		// 3) Print any remaining observations up to 20, covering the time up to tau
		// This loop ensures that all 20 observations are printed, even if the PEC runs out of events early
		// or the simulation breaks due to currentTime > tau.
		while (obsNum <= 20) {
			Individual best = findBestIndividual();
			boolean hit = best != null && best.getLastPosition().equals(context.getDestino());
			printObservation(obsNum, obsNum * obsInterval, events, // Use obsNum * obsInterval for time for consistency
					context.getPopulation().getALL().size(),
					hit, best);
			obsNum++;
		}

		// 4) Print the final "Best fit individual" at the very end of the simulation
		Individual best = findBestIndividual();
		printBestFitIndividual(best);
	}

	/**
	 * Prints the initial simulation parameters to the console.
	 * This includes grid dimensions, start/end points, counts of special zones/obstacles,
	 * and various simulation constants. Special cost zones and obstacles are listed
	 * in a detailed format if present.
	 *
	 * @param params The {@link SimulationParameters} object containing all initial settings.
	 */
	private void printInputParameters(SimulationParameters params) {
		System.out.printf("%d %d %d %d %d %d %d %d %d %d %d %d %d %d %d%n",
				params.getN(), params.getM(),
				params.getStartPoint().getX(), params.getStartPoint().getY(),
				params.getEndPoint().getX(), params.getEndPoint().getY(),
				params.getSpecialCostZones().size(), params.getObstacles().size(),
				params.getTau(), params.getNu(), params.getNuMax(),
				params.getK(), (int) params.getMu(), (int) params.getDelta(), (int) params.getRho());

		if (!params.getSpecialCostZones().isEmpty()) {
			System.out.println("special cost zones:");
			for (Special_Cost_Zone zone : params.getSpecialCostZones()) {
				System.out.printf("%d %d %d %d %d%n",
						zone.getInf().getX(), zone.getInf().getY(),
						zone.getSup().getX(), zone.getSup().getY(),
						zone.getCusto());
			}
		}
		if (!params.getObstacles().isEmpty()) {
			System.out.println("obstacles:");
			for (Coordenadas c : params.getObstacles()) {
				System.out.printf("%d %d%n", c.getX(), c.getY());
			}
		}
		System.out.println();
		System.out.println();
	}

	/**
	 * Prints the details for a specific observation point during the simulation.
	 * This includes the observation number, current time, number of events processed so far,
	 * current population size, whether the target has been reached, and details of the best
	 * fit individual (its path and either cost if target reached, or comfort).
	 *
	 * @param obsNum The sequential number of this observation.
	 * @param time The simulated time at which this observation is made.
	 * @param events The total number of events processed up to this observation time.
	 * @param popSize The current total size of the population (alive + dead individuals).
	 * @param hit A boolean indicating if the best individual has reached the final point.
	 * @param best The {@link Individual} identified as the best fit at this observation time, or {@code null} if none.
	 */
	private void printObservation(int obsNum, int time, int events,
								  int popSize, boolean hit, Individual best) {
		System.out.println("Observation " + obsNum + ":");
		System.out.println("Present time: " + time);
		System.out.println("Number of realized events: " + events);
		System.out.println("Population size: " + popSize);
		System.out.println("Final point has been hit: " + (hit ? "yes" : "no"));
		System.out.print("Path of the best fit individual: ");
		if (best != null) {
			System.out.print("[");
			List<Coordenadas> path = best.getPath();
			for (int i = 0; i < path.size(); i++) {
				System.out.print(path.get(i));
				if (i < path.size() - 1) System.out.print(", ");
			}
			System.out.println("]");
			if (hit) {
				System.out.println("Cost/Comfort: " + best.getCost(context.getGrid()));
			} else {
				System.out.println("Cost/Comfort: " +
						String.format("%.6f",
								best.getComfort(context.getGrid(), context.getDestino(), context.getK())
						));
			}
		} else {
			System.out.println("[]");
			System.out.println("Cost/Comfort: 0"); // If no best individual, output 0 for cost/comfort
		}
		System.out.println();
	}

	/**
	 * Prints the final "best fit" individual identified at the end of the simulation.
	 * This includes its path and the final cost.
	 *
	 * @param best The {@link Individual} identified as the best fit at the end, or {@code null} if none.
	 */
	private void printBestFitIndividual(Individual best) {
		System.out.print("Best fit individual: ");
		if (best != null) {
			System.out.print("[");
			List<Coordenadas> path = best.getPath();
			for (int i = 0; i < path.size(); i++) {
				System.out.print(path.get(i));
				if (i < path.size() - 1) System.out.print(", ");
			}
			System.out.println("] with cost " + best.getCost(context.getGrid()));
		} else {
			System.out.println("[] with cost 0"); // If no best individual found, output 0 cost
		}
	}

	/**
	 * Finds the "best fit" individual among all individuals in the population.
	 * The criteria for "best fit" are:
	 * <ol>
	 * <li>Any individual that has reached the target destination,
	 * prioritizing the one with the lowest path cost.</li>
	 * <li>If no individual has reached the target, then the individual
	 * with the highest "comfort" level among all individuals.</li>
	 * </ol>
	 *
	 * @return The {@link Individual} deemed best fit according to the criteria,
	 * or {@code null} if the population is empty.
	 */
	private Individual findBestIndividual() {
		List<Individual> all = context.getPopulation().getALL();
		Coordenadas destino = context.getDestino();
		Grid grid = context.getGrid();
		int k = context.getK();

		// 1) First, find the individual that has reached the destination with the lowest cost
		Individual bestReached = null;
		int minCost = Integer.MAX_VALUE;
		for (Individual ind : all) {
			if (ind.getLastPosition().equals(destino)) {
				int cost = ind.getCost(grid);
				if (cost < minCost) {
					minCost = cost;
					bestReached = ind;
				}
			}
		}
		// If at least one individual reached the destination, return the best among them
		if (bestReached != null) return bestReached;

		// 2) If no individual reached the destination, find the one with the highest comfort
		Individual bestComfort = null;
		double maxComfort = -1.0; // Initialize with a value lower than any possible comfort
		for (Individual ind : all) {
			// Calculate comfort for individuals that haven't reached the destination
			double comfort = ind.getComfort(grid, destino, k);
			if (comfort > maxComfort) {
				maxComfort = comfort;
				bestComfort = ind;
			}
		}
		// Return the individual with the highest comfort, or null if population is empty
		return bestComfort;
	}
}