package MODE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import MODE.Grid;
import MODE.Coordenadas;

/**
 * Represents an individual entity within the simulation that navigates a grid,
 * can reproduce, and has a lifecycle defined by birth and death times.
 * Each individual maintains a path of visited coordinates and has associated
 * properties like cost and comfort of its path.
 */
public class Individual {
	/**
	 * The list of {@link Coordenadas} representing the path traversed by the individual.
	 */
	private List<Coordenadas> path;
	/**
	 * The current x-coordinate of the individual's position on the grid.
	 */
	private int x;
	/**
	 * The current y-coordinate of the individual's position on the grid.
	 */
	private int y;
	/**
	 * The simulated time at which this individual was "born" or created.
	 */
	private int birthTime;
	/**
	 * The simulated time at which this individual is scheduled to "die".
	 */
	private int deathTime;
	/**
	 * A reference to the parent individual, if this individual was created through reproduction.
	 * Can be {@code null} for initial individuals.
	 */
	private Individual parent;
	/**
	 * A boolean flag indicating whether this individual has already reproduced.
	 */
	private boolean reproduced;

	/**
	 * Constructs a new Individual with a specified starting position, birth time, and death time.
	 * This constructor is typically used for initial individuals in the simulation.
	 *
	 * @param start The initial {@link Coordenadas} of the individual.
	 * @param birth The simulated time of birth.
	 * @param death The simulated time of death.
	 */
	public Individual(Coordenadas start, int birth, int death) {
		this.path = new ArrayList<>();
		this.x = start.getX();
		this.y = start.getY();
		this.path.add(start);
		this.birthTime = birth;
		this.deathTime = death;
		this.parent = null;
		this.reproduced = false;
	}

	/**
	 * Constructs a new Individual that is a child of an existing parent, inheriting
	 * a portion of the parent's path.
	 *
	 * @param start The initial {@link Coordenadas} of the child, which should be the last coordinate of the inherited path.
	 * @param birth The simulated time of birth.
	 * @param death The simulated time of death.
	 * @param parent The parent {@link Individual} from whom this individual was reproduced.
	 * @param inheritedPath A {@link List} of {@link Coordenadas} representing the path inherited from the parent.
	 */
	public Individual(Coordenadas start, int birth, int death, Individual parent, List<Coordenadas> inheritedPath) {
		this.path = new ArrayList<>(inheritedPath);
		this.x = start.getX();
		this.y = start.getY();
		// Ensure the start position is included if not already the last one in the inherited path
		if (this.path.isEmpty() || !this.path.get(this.path.size() - 1).equals(start)) {
			this.path.add(start);
		}
		this.birthTime = birth;
		this.deathTime = death;
		this.parent = parent;
		this.reproduced = false;
	}

	/**
	 * Returns the current x-coordinate of the individual.
	 * @return The x-coordinate.
	 */
	public int getX() { return x; }
	/**
	 * Returns the current y-coordinate of the individual.
	 * @return The y-coordinate.
	 */
	public int getY() { return y; }
	/**
	 * Returns the current position of the individual as a {@link Coordenadas} object.
	 * @return The current {@link Coordenadas}.
	 */
	public Coordenadas getCurrentPosition() { return new Coordenadas(x, y); }
	/**
	 * Returns the last position recorded in the individual's path.
	 * @return The {@link Coordenadas} of the last visited point.
	 */
	public Coordenadas getLastPosition() { return path.get(path.size() - 1); }
	/**
	 * Returns the complete path traversed by the individual.
	 * @return A {@link List} of {@link Coordenadas} representing the individual's path.
	 */
	public List<Coordenadas> getPath() { return path; }
	/**
	 * Returns the length of the individual's path (number of edges traversed).
	 * This is calculated as the number of coordinates in the path minus one.
	 * @return The length of the path.
	 */
	public int getLength() { return path.size() - 1; }
	/**
	 * Returns the simulated birth time of the individual.
	 * @return The birth time.
	 */
	public int getBirthTime() { return birthTime; }
	/**
	 * Returns the simulated death time of the individual.
	 * @return The death time.
	 */
	public int getDeathTime() { return deathTime; }
	/**
	 * Sets the simulated birth time of the individual.
	 * @param t The new birth time.
	 */
	public void setBirthTime(int t) { this.birthTime = t; }
	/**
	 * Sets the simulated death time of the individual.
	 * @param t The new death time.
	 */
	public void setDeathTime(int t) { this.deathTime = t; }

	/**
	 * Moves the individual to the specified next coordinate, updating its current position
	 * and adding the new coordinate to its path. After moving, it attempts to remove
	 * any cycles from the path to optimize it.
	 *
	 * @param next The {@link Coordenadas} to which the individual will move.
	 */
	public void moveTo(Coordenadas next) {
		this.x = next.getX();
		this.y = next.getY();
		this.path.add(next);
		removeCycles();
	}

	/**
	 * Optimizes the individual's path by removing any cycles (loops).
	 * If a previously visited coordinate is encountered, the path is truncated
	 * back to the first occurrence of that coordinate, effectively removing the loop.
	 * This ensures the path is a simple path (no repeated vertices except for the start/end in a cycle).
	 */
	private void removeCycles() {
		Set<Coordenadas> seen = new HashSet<>();
		List<Coordenadas> newPath = new ArrayList<>();
		for (Coordenadas coord : path) {
			if (seen.contains(coord)) {
				// If a cycle is detected, truncate the path back to the point of cycle
				int idx = newPath.indexOf(coord);
				newPath = new ArrayList<>(newPath.subList(0, idx + 1));
			} else {
				newPath.add(coord);
				seen.add(coord);
			}
		}
		this.path = newPath;
	}

	/**
	 * Calculates the total cost of the individual's current path on the given grid.
	 * The cost is determined by summing the costs of individual edges, as provided by the grid.
	 *
	 * @param grid The {@link Grid} on which the path exists, used to determine edge costs.
	 * @return The total accumulated cost of the path.
	 */
	public int getCost(Grid grid) {
		int cost = 0;
		for (int i = 1; i < path.size(); i++) {
			Coordenadas a = path.get(i - 1);
			Coordenadas b = path.get(i);
			cost += grid.custoCaminho(a, b);
		}
		return cost;
	}

	/**
	 * Calculates a "comfort" metric for the individual's current path.
	 * This metric is influenced by the path's cost, length, the maximum possible
	 * edge cost in the grid, and the Manhattan distance to the target.
	 * The formula used is based on the problem description's comfort function.
	 *
	 * @param grid The {@link Grid} context for path cost and max edge cost.
	 * @param target The {@link Coordenadas} of the target destination.
	 * @param k A weighting factor applied to the comfort calculation.
	 * @return The calculated comfort value, normalized between 0.001 and 0.999.
	 */
	public double getComfort(Grid grid, Coordenadas target, int k) {
		int length = getLength();
		int cost = getCost(grid);
		int maxEdgeCost = grid.getMaxCustoAresta();
		int distToEnd = getLastPosition().getDistancia(target);

		// Component related to path cost and length
		// The original formula: (1 - cost - length + 2) / ((maxEdgeCost - 1) * length + 3)
		// Assuming 'cost' here refers to the total cost and 'length' is number of edges.
		// Added explicit 1.0 to ensure double division.
		double part1 = (1.0 - cost - length + 2) / ((maxEdgeCost - 1.0) * length + 3);

		// Component related to distance to target
		// Assuming grid.getN() + grid.getM() + 1 represents a maximum possible distance or normalization factor
		double part2 = 1.0 - ((double) distToEnd / (grid.getN() + grid.getM() + 1));

		// Clamp values to avoid extreme comfort values
		part1 = Math.max(0.001, Math.min(0.999, part1));
		part2 = Math.max(0.001, Math.min(0.999, part2));

		return Math.pow(part1, k) * Math.pow(part2, k);
	}

	/**
	 * Calculates the potential comfort of the individual's path if it were to move
	 * to a hypothetical new position. This method temporarily adds the new position
	 * to the path, calculates comfort, and then reverts the path and position.
	 * This is useful for evaluating potential next moves.
	 *
	 * @param grid The {@link Grid} context for path cost and max edge cost.
	 * @param pos The hypothetical next {@link Coordenadas} to evaluate.
	 * @param targetIndex An index used to derive the target coordinates (e.g., in a flat grid representation).
	 * Assumes target is derived from (targetIndex / m) + 1 and (targetIndex % m) + 1.
	 * @param k A weighting factor applied to the comfort calculation.
	 * @return The calculated comfort value if the individual were to move to 'pos'.
	 */
	public double getComfortAtPosition(Grid grid, Coordenadas pos, int targetIndex, int k) {
		int m = grid.getM();
		// Convert targetIndex to 1-indexed (x, y) coordinates
		int tx = (targetIndex / m) + 1;
		int ty = (targetIndex % m) + 1;
		Coordenadas target = new Coordenadas(tx, ty);

		// Store current state to revert later
		Coordenadas oldLast = getLastPosition(); // Note: This might not be directly used for revert unless path.remove is before this.
												 // The x, y are directly set below, and path is removed by index.
		int oldX = this.x;
		int oldY = this.y;

		// Temporarily add new position and update current coordinates
		path.add(pos);
		this.x = pos.getX();
		this.y = pos.getY();

		double comfort = getComfort(grid, target, k);

		// Revert to original state
		path.remove(path.size() - 1); // Remove the temporarily added position
		this.x = oldX;
		this.y = oldY;
		return comfort;
	}

	/**
	 * Creates a new child individual by "reproducing" from the current individual.
	 * The child inherits a prefix of the parent's path, with the length of the prefix
	 * being determined by a formula involving a 'comfort' factor (phi).
	 * Specifically, the child inherits 90% of the parent's path plus an additional
	 * percentage (up to 10%) based on the parent's comfort.
	 * The child is initialized with birth and death times typically set to 0 and 0,
	 * implying they will be set later by the simulation context.
	 *
	 * @param k A weighting factor used in the comfort calculation for determining the inherited path length.
	 * @param grid The {@link Grid} context for calculating the parent's comfort.
	 * @param target The {@link Coordenadas} of the simulation's target destination, used for comfort calculation.
	 * @return A new {@link Individual} representing the child.
	 */
	public Individual reproduz(int k, Grid grid, Coordenadas target) {
		double phi = getComfort(grid, target, k);
		int vertices = path.size();
		// Formula for prefix count: 90% of parent's path + phi * 10% of remaining path
		int prefixCount = (int) Math.ceil(vertices * 0.9 + vertices * 0.1 * phi);
		// Ensure prefixCount is at least 1 and not more than the total vertices
		prefixCount = Math.max(1, Math.min(prefixCount, vertices));
		List<Coordenadas> inheritedPath = new ArrayList<>(path.subList(0, prefixCount));
		Coordenadas start = inheritedPath.get(inheritedPath.size() - 1); // Child starts at the end of inherited path
		return new Individual(start, 0, 0, this, inheritedPath);
	}
}