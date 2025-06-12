package MODE;

/**
 * Represents a coordinate point (x, y) on the grid.
 * This class is used to define positions for various elements
 * within the simulation, such as individual locations, obstacles,
 * and special cost zones.
 */
public class Coordenadas {
	/** The x-coordinate of the point. */
	private int x;
	/** The y-coordinate of the point. */
	private int y;

    /**
     * Constructs a new Coordenadas object with the specified x and y coordinates.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
	public Coordenadas(int x, int y) {
		this.x = x;
		this.y = y;
	}

    /**
     * Returns the x-coordinate of this point.
     * @return The x-coordinate.
     */
	public int getX() {
		return x;
	}

    /**
     * Returns the y-coordinate of this point.
     * @return The y-coordinate.
     */
	public int getY() {
		return y;
	}

    /**
     * Calculates the Manhattan distance (L1 norm) between this coordinate
     * and another specified coordinate. The Manhattan distance is the sum
     * of the absolute differences of their coordinates.
     *
     * @param outro The other Coordenadas object to which the distance is calculated.
     * @return The Manhattan distance between this coordinate and the 'outro' coordinate.
     */
	public int getDistancia(Coordenadas outro) {
		return Math.abs(this.x - outro.x) + Math.abs(this.y - outro.y);
	}
    /**
     * Returns a string representation of the Coordenadas object in the format "(x,y)".
     * @return A string representation of the point.
     */
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
    /**
     * Compares this Coordenadas object to another object to check for equality.
     * Two Coordenadas objects are considered equal if their x and y coordinates are the same.
     * @param obj The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Coordenadas)) return false;
		Coordenadas other = (Coordenadas) obj;
		return this.x == other.x && this.y == other.y;
	}
    /**
     * Returns a hash code value for the object. This method is supported for the benefit
     * of hash tables such as those provided by {@link java.util.HashMap}.
     * @return A hash code value for this object.
     */
	@Override
	public int hashCode() {
		return 31 * x + y;
	}
}
