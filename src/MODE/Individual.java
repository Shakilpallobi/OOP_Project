package MODE;
import java.util.*;



public class Individual {
	private List<Coordenadas> path;
	private int birthTime;
	private int deathTime;
	private Individual parent;

	public Individual(Coordenadas start, int birth, int death) {
		this.path = new ArrayList<>();
		this.path.add(start);
		this.birthTime = birth;
		this.deathTime = death;
		this.parent = null;
	}

	public Individual(Coordenadas start, int birth, int death, Individual parent, List<Coordenadas> inheritedPath) {
		this.path = new ArrayList<>(inheritedPath);
		this.birthTime = birth;
		this.deathTime = death;
		this.parent = parent;
	}

	public Coordenadas getLastPosition() {
		return path.get(path.size() - 1);
	}

	public List<Coordenadas> getPath() {
		return path;
	}

	public int getLength() {
		return path.size() - 1; // número de arestas, não de nós
	}

	/**
	 * Calcula o custo do caminho até agora.
	 */
	public int getCost(Grid grid) {
		int cost = 0;
		for (int i = 1; i < path.size(); i++) {
			cost += grid.custoCaminho(path.get(i - 1), path.get(i));
		}
		return cost;
	}

	/**
	 * Calcula o conforto φ(z).
	 */
	public double getComfort(Grid grid, Coordenadas target, int k) {
		int length = getLength();
		int cost = getCost(grid);
		int maxEdgeCost = grid.getMaxCustoAresta();
		int distToEnd = getLastPosition().getDistancia(target);

		// Fórmula corrigida conforme enunciado
		double part1 = (1.0 - cost - length + 2) / ((maxEdgeCost - 1.0) * length + 3);
		double part2 = 1.0 - ((double) distToEnd / (grid.getN() + grid.getM() + 1));

		// Clamping para manter valores válidos
		part1 = Math.max(0.001, Math.min(0.999, part1));
		part2 = Math.max(0.001, Math.min(0.999, part2));

		return Math.pow(part1, k) * Math.pow(part2, k);
	}


	/**
	 * Gera um novo indivíduo filho com base no caminho parcial deste.
	 */
	public Individual reproduz(int k) {
		int total = path.size();
		int baseSize = (int) Math.ceil(0.9 * total);
		int extra = (int) Math.ceil(k * 0.1 * total);
		int size = Math.min(total, baseSize + extra);

		List<Coordenadas> childPath = new ArrayList<>(path.subList(0, size));
		// tempo de nascimento e morte devem ser definidos no evento de reprodução
		return new Individual(childPath.get(0), 0, 0, this, childPath);
	}

	/**
	 * Adiciona uma nova coordenada e elimina ciclos, se necessário.
	 */
	public void move(Coordenadas next) {
		path.add(next);
		removeCycles();
	}

	private void removeCycles() {
		Set<Coordenadas> seen = new HashSet<>();
		List<Coordenadas> newPath = new ArrayList<>();

		for (Coordenadas coord : path) {
			if (seen.contains(coord)) {
				// corta até à última ocorrência anterior
				int idx = newPath.indexOf(coord);
				newPath = new ArrayList<>(newPath.subList(0, idx + 1));
			} else {
				newPath.add(coord);
				seen.add(coord);
			}
		}

		this.path = newPath;
	}

	public int getBirthTime() {
		return birthTime;
	}

	public int getDeathTime() {
		return deathTime;
	}

	public void setBirthTime(int birthTime) {
		this.birthTime = birthTime;
	}

	public void setDeathTime(int deathTime) {
		this.deathTime = deathTime;
	}

	@Override
	public String toString() {
		return "Individual{" +
				"path=" + path +
				", cost=" + getLength() +
				", birth=" + birthTime +
				", death=" + deathTime +
				'}';
	}
}
