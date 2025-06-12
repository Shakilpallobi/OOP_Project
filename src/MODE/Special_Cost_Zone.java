package MODE;

/**
 * Represents a special cost zone within the grid.
 * A special cost zone is a rectangular area defined by two corner coordinates
 * (inferior-left and superior-right) and an associated movement cost.
 * Movement edges that are part of the perimeter of this zone will incur this special cost.
 */
public class Special_Cost_Zone {
	/**
	 * The {@link Coordenadas} of the inferior-left corner of the special cost zone.
	 */
	private Coordenadas cantoInferior;
	/**
	 * The {@link Coordenadas} of the superior-right corner of the special cost zone.
	 */
	private Coordenadas cantoSuperior;
	/**
	 * The cost associated with moving along an edge that is part of this special cost zone's perimeter.
	 */
	private int custo;

	/**
	 * Constructs a new Special_Cost_Zone with specified corner coordinates and cost.
	 *
	 * @param inf The {@link Coordenadas} of the inferior-left corner of the zone.
	 * @param sup The {@link Coordenadas} of the superior-right corner of the zone.
	 * @param custo The movement cost associated with this zone.
	 */
	public Special_Cost_Zone(Coordenadas inf, Coordenadas sup, int custo) {
		this.cantoInferior = inf;
		this.cantoSuperior = sup;
		this.custo = custo;
	}

	/**
	 * Returns the movement cost associated with this special cost zone.
	 * @return The cost value.
	 */
	public int getCusto() {
		return custo;
	}

	/**
	 * Returns the {@link Coordenadas} of the inferior-left corner of the zone.
	 * @return The inferior corner coordinates.
	 */
	public Coordenadas getCantoInferior() {
		return cantoInferior;
	}

	/**
	 * Returns the {@link Coordenadas} of the superior-right corner of the zone.
	 * @return The superior corner coordinates.
	 */
	public Coordenadas getCantoSuperior() {
		return cantoSuperior;
	}

	/**
	 * Provides an alternative getter for the inferior-left corner,
	 * ensuring compatibility with other parts of the codebase that might expect {@code getInf()}.
	 * @return The {@link Coordenadas} of the inferior-left corner.
	 */
	public Coordenadas getInf() {
		return getCantoInferior();
	}

	/**
	 * Provides an alternative getter for the superior-right corner,
	 * ensuring compatibility with other parts of the codebase that might expect {@code getSup()}.
	 * @return The {@link Coordenadas} of the superior-right corner.
	 */
	public Coordenadas getSup() {
		return getCantoSuperior();
	}

	/**
	 * Checks if a movement edge between two adjacent coordinates is affected by this special cost zone.
	 * An edge is affected if both its 'from' and 'to' coordinates lie on the perimeter of this zone.
	 *
	 * @param from The starting {@link Coordenadas} of the movement.
	 * @param to The ending {@link Coordenadas} of the movement.
	 * @return {@code true} if the edge is affected by this zone's cost, {@code false} otherwise.
	 * @throws IllegalArgumentException if 'from' and 'to' are not adjacent (Manhattan distance not equal to 1).
	 */
	public boolean afetaAresta(Coordenadas from, Coordenadas to) {
		// Ensure the coordinates are adjacent before checking if they are on the perimeter.
		// A distance of 1 means they are directly horizontal or vertical neighbors.
		if (Math.abs(from.getX() - to.getX()) + Math.abs(from.getY() - to.getY()) != 1) {
			// This check is implicitly handled by the original code's throw, but good to be explicit for a robust method.
			// Alternatively, could throw IllegalArgumentException("Coordenadas não são adjacentes.");
			return false; // Not adjacent, so cannot be an edge affected by the zone.
		}
		// An edge is affected if both its endpoints are on the perimeter of the zone.
		return isOnPerimeter(from) && isOnPerimeter(to);
	}

	/**
	 * Checks if a given coordinate lies on the perimeter (border) of this special cost zone.
	 * This means the coordinate's x or y value must match one of the boundary values of the zone
	 * (x1, x2, y1, y2) while also being within the other dimension's range.
	 *
	 * @param c The {@link Coordenadas} to check.
	 * @return {@code true} if the coordinate is on the perimeter, {@code false} otherwise.
	 */
	private boolean isOnPerimeter(Coordenadas c) {
		int x = c.getX();
		int y = c.getY();
		int x1 = cantoInferior.getX();
		int y1 = cantoInferior.getY();
		int x2 = cantoSuperior.getX();
		int y2 = cantoSuperior.getY();

		// Check if coordinate is within the horizontal range [x1, x2]
		boolean dentroHorizontal = x >= x1 && x <= x2;
		// Check if coordinate is within the vertical range [y1, y2]
		boolean dentroVertical = y >= y1 && y <= y2;

		// Check if coordinate is on one of the horizontal borders (top or bottom edge)
		// and horizontally within the zone.
		boolean naBordaHorizontal = (y == y1 || y == y2) && dentroHorizontal;
		// Check if coordinate is on one of the vertical borders (left or right edge)
		// and vertically within the zone.
		boolean naBordaVertical = (x == x1 || x == x2) && dentroVertical;

		// A coordinate is on the perimeter if it's on a horizontal border OR a vertical border.
		return naBordaHorizontal || naBordaVertical;
	}

	/**
	 * Returns a string representation of the special cost zone,
	 * including its corner coordinates and associated cost.
	 *
	 * @return A formatted string describing the special cost zone.
	 */
	@Override
	public String toString() {
		return "Zona especial: [" + cantoInferior + " -> " + cantoSuperior + "] custo = " + custo;
	}
}