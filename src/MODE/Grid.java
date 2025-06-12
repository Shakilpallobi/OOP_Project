package MODE;

import java.util.*;

/**
 * Represents the grid environment for pathfinding, defined by its dimensions,
 * obstacles, and special cost zones.
 * This class manages the grid's properties and provides methods to query
 * its state, such as checking for obstacles, determining movement costs,
 * and finding valid adjacent moves.
 */
public class Grid {
	/**
	 * The number of rows in the grid (n).
	 */
	private int n; // linhas
	/**
	 * The number of columns in the grid (m).
	 */
	private int m; // colunas
	/**
	 * A set of {@link Coordenadas} representing points on the grid that are obstacles.
	 * Movement through an obstacle is not permitted.
	 */
	private Set<Coordenadas> obstacles;
	/**
	 * A list of {@link Special_Cost_Zone} objects, each defining an area
	 * where movement edges have a cost higher than the default.
	 */
	private List<Special_Cost_Zone> costZones;

	/**
	 * Constructs a new Grid with the specified dimensions.
	 * Initializes the grid with no obstacles and no special cost zones.
	 *
	 * @param n The number of rows in the grid. Must be a positive integer.
	 * @param m The number of columns in the grid. Must be a positive integer.
	 */
	public Grid(int n, int m) {
		this.n = n;
		this.m = m;
		this.obstacles = new HashSet<>();
		this.costZones = new ArrayList<>();
	}

	/**
	 * Returns the number of rows (n) in the grid.
	 * @return The number of rows.
	 */
	public int getN() {
		return n;
	}

	/**
	 * Returns the number of columns (m) in the grid.
	 * @return The number of columns.
	 */
	public int getM() {
		return m;
	}

	/**
	 * Adds an obstacle to the grid at the specified coordinates.
	 * Once a coordinate is marked as an obstacle, movement through it is disallowed.
	 *
	 * @param c The {@link Coordenadas} of the point to be marked as an obstacle.
	 */
	public void addObstacle(Coordenadas c) {
		obstacles.add(c);
	}

	/**
	 * Adds a special cost zone to the grid.
	 * Edges within or crossing these zones may have a higher movement cost.
	 *
	 * @param zone The {@link Special_Cost_Zone} to add to the grid.
	 */
	public void addSpecialCostZone(Special_Cost_Zone zone) {
		costZones.add(zone);
	}

	/**
	 * Checks if the given coordinates represent an obstacle on the grid.
	 *
	 * @param c The {@link Coordenadas} to check.
	 * @return {@code true} if the coordinates point to an obstacle, {@code false} otherwise.
	 */
	public boolean isObstacle(Coordenadas c) {
		return obstacles.contains(c);
	}

	/**
	 * Returns the cost of moving between two adjacent coordinates.
	 * If the movement path (edge) is within or crosses a special cost zone,
	 * the highest cost defined by any overlapping special cost zone is returned.
	 * Otherwise, the default cost of 1 is returned.
	 *
	 * @param from The starting {@link Coordenadas} of the movement.
	 * @param to The ending {@link Coordenadas} of the movement.
	 * @return The cost of traversing the edge between 'from' and 'to'.
	 * @throws IllegalArgumentException if the 'from' and 'to' coordinates are not adjacent.
	 */
	public int custoCaminho(Coordenadas from, Coordenadas to) {
		if (!saoAdjacentes(from, to)) {
			throw new IllegalArgumentException("Coordenadas não são adjacentes.");
		}

		int custoMax = 1;
		for (Special_Cost_Zone zone : costZones) {
			if (zone.afetaAresta(from, to)) {
				custoMax = Math.max(custoMax, zone.getCusto());
			}
		}
		return custoMax;
	}

	/**
	 * Returns the maximum edge cost present in any of the special cost zones
	 * defined on the grid. If no special cost zones exist, the default edge cost of 1 is returned.
	 *
	 * @return The highest cost of any special cost zone edge.
	 */
	public int getMaxCustoAresta() {
		int max = 1;
		for (Special_Cost_Zone zone : costZones) {
			max = Math.max(max, zone.getCusto());
		}
		return max;
	}

	/**
	 * Returns a list of all valid adjacent {@link Coordenadas} from a given current coordinate.
	 * A move is considered valid if it remains within the grid boundaries and does not
	 * lead to an obstacle.
	 *
	 * @param atual The current {@link Coordenadas} from which to find valid moves.
	 * @return A {@link List} of {@link Coordenadas} representing all accessible and non-obstacle adjacent points.
	 */
	public List<Coordenadas> getValidMoves(Coordenadas atual) {
		List<Coordenadas> moves = new ArrayList<>();
		int x = atual.getX();
		int y = atual.getY();

		// Define directions for movement (North, East, South, West)
		int[][] direcoes = {
				{0, 1},   // Norte (y+1)
				{1, 0},   // Este (x+1)
				{0, -1},  // Sul (y-1)
				{-1, 0}   // Oeste (x-1)
		};

		for (int[] d : direcoes) {
			Coordenadas nova = new Coordenadas(x + d[0], y + d[1]);
			if (estaDentro(nova) && !isObstacle(nova)) {
				moves.add(nova);
			}
		}

		return moves;
	}

	/**
	 * Checks if a given coordinate is within the boundaries of this grid.
	 * Coordinates are 1-indexed (from 1 to n/m).
	 *
	 * @param c The {@link Coordenadas} to check.
	 * @return {@code true} if the coordinates are within the grid, {@code false} otherwise.
	 */
	private boolean estaDentro(Coordenadas c) {
		return c.getX() >= 1 && c.getX() <= n && c.getY() >= 1 && c.getY() <= m;
	}

	/**
	 * Checks if two coordinates are directly adjacent (i.e., horizontally or vertically)
	 * and not the same point. Adjacency is defined by a Manhattan distance of 1.
	 *
	 * @param a The first {@link Coordenadas}.
	 * @param b The second {@link Coordenadas}.
	 * @return {@code true} if the coordinates are adjacent, {@code false} otherwise.
	 */
	private boolean saoAdjacentes(Coordenadas a, Coordenadas b) {
		int dx = Math.abs(a.getX() - b.getX());
		int dy = Math.abs(a.getY() - b.getY());
		return dx + dy == 1;
	}
}