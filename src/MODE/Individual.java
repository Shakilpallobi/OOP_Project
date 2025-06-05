package MODE;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Individual {
	private List<Coordenadas> path;
	private int x, y;             // posição atual
	private int birthTime;
	private int deathTime;
	private Individual parent;
	private boolean reproduced;   // marca se já reproduziu neste passo

	/**
	 * Construtor usado em CORE.Simulation(int n, ...).
	 * Escolhe uma posição inicial aleatória num grid n×n, e coloca-a no path.
	 */
	public Individual(int n) {
		this.path = new ArrayList<>();
		Random rnd = new Random();
		// coordenadas válidas de 1 a n
		this.x = rnd.nextInt(n) + 1;
		this.y = rnd.nextInt(n) + 1;
		this.path.add(new Coordenadas(x, y));
		this.birthTime = 0;
		this.deathTime = Integer.MAX_VALUE; // poderá ser ajustado noutro lado
		this.parent = null;
		this.reproduced = false;
	}

	/**
	 * Construtor original (mantido) que cria o indivíduo a partir de uma coordenada dada,
	 * definindo birthTime e deathTime.
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
	 * Se houver necessidade de inicializar outros atributos (por ex. genes), faz aqui.
	 */
	public void initialize() {
		// … código de inicialização adicional, se necessário
	}

	// —————————————————————————————————————————————
	// GETTERS/SETTERS exigidos pelos “events” em CORE:
	// —————————————————————————————————————————————

	/** Retorna a posição atual X */
	public int getX() {
		return x;
	}

	/** Retorna a posição atual Y */
	public int getY() {
		return y;
	}

	/**
	 * Define a posição X e adiciona automaticamente ao path a nova Coordenadas(x,y).
	 */
	public void setX(int newX) {
		this.x = newX;
		this.path.add(new Coordenadas(this.x, this.y));
	}

	/**
	 * Define a posição Y e adiciona automaticamente ao path a nova Coordenadas(x,y).
	 */
	public void setY(int newY) {
		this.y = newY;
		this.path.add(new Coordenadas(this.x, this.y));
	}

	/**
	 * Move explicitamente para a coordenada next, sincronizando x,y e adicionando ao path.
	 */
	public void moveTo(Coordenadas next) {
		this.x = next.getX();
		this.y = next.getY();
		this.path.add(next);
	}

	/** Retorna a coordenada atual (último elemento do path) */
	public Coordenadas getCurrentPosition() {
		return new Coordenadas(x, y);
	}

	/** Uma outra forma de chamar “posição atual” */
	public Coordenadas getLastPosition() {
		return getCurrentPosition();
	}

	/** Retorna o comprimento atual do percurso (tamanho de path) */
	public int getLength() {
		return path.size();
	}

	/** Alias para getLength(), “custo” do caminho até agora */
	public int getCost() {
		return getLength();
	}

	/** Retorna todo o path percorrido (lista de Coordenadas) */
	public List<Coordenadas> getPath() {
		return path;
	}

	/** Retorna a hora de nascimento */
	public int getBirthTime() {
		return birthTime;
	}

	/** Define a hora de nascimento */
	public void setBirthTime(int birthTime) {
		this.birthTime = birthTime;
	}

	/** Retorna a hora de morte prevista */
	public int getDeathTime() {
		return deathTime;
	}

	/** Define a hora de morte prevista */
	public void setDeathTime(int deathTime) {
		this.deathTime = deathTime;
	}

	/** Marca ou desmarca se já reproduziu neste passo */
	public boolean isReproduced() {
		return reproduced;
	}

	/** Define se já reproduziu neste passo */
	public void setReproduced(boolean reproduced) {
		this.reproduced = reproduced;
	}

	/** Retorna o pai (usado para linha genética, se necessário) */
	public Individual getParent() {
		return parent;
	}

	/** Define o pai (usado para linha genética, se necessário) */
	public void setParent(Individual parent) {
		this.parent = parent;
	}

	/**
	 * Cria um novo Individual “filho” combinando/geneticamente a partir deste e do outro.
	 * Aqui está só um exemplo “clonado” sem lógica real de genes.
	 */
	public Individual reproduceWith(Individual outro) {
		// Exemplo mínimo: cria um filho na posição do pai (neste caso, “this”)
		Coordenadas spawn = this.getCurrentPosition();
		Individual filho = new Individual(spawn,
				Math.max(this.birthTime, outro.birthTime),
				Math.min(this.deathTime, outro.deathTime));
		filho.setParent(this);
		return filho;
	}

	/**
	 * Calcula e retorna o “conforto” deste indivíduo na sua posição atual,
	 * dado o grid, o destino (célula alvo) e o parâmetro k.
	 *
	 * Aqui está uma implementação de exemplo baseada em distância Manhattan.
	 * Ajusta conforme a tua função de conforto real.
	 */
	public double getComfort(int[][] grid, int destino, int k) {
		// Exemplo: converte “destino” num par de coordenadas (Dx, Dy).
		// Supondo que destino é, por exemplo, um índice linear ou uma célula
		// cujo valor em grid[x][y] == destino.
		// Para simplificar: encontra primeiro a célula cujo grid[cx][cy] == destino.
		int n = grid.length; // assumindo grid quadrado de tamanho n×n
		int dx = -1, dy = -1;
		outer:
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (grid[i][j] == destino) {
					dx = i + 1; // convertendo para “1-based”, se usas 1-based em Individual
					dy = j + 1;
					break outer;
				}
			}
		}
		if (dx == -1) {
			// Se não encontrou destino no grid, retorna conforto neutro
			return 0.0;
		}
		// Distância Manhattan entre (x,y) e (dx,dy):
		int dist = Math.abs(this.x - dx) + Math.abs(this.y - dy);
		// Normalizar: maior distância possível num n×n é 2*(n-1).
		double maxDist = 2.0 * (n - 1);
		double normalized = 1.0 - ((double) dist / maxDist);
		// Se quiseres incluir “k” no cálculo, podes fazer algo como:
		return Math.max(0.0, normalized * (1.0 - ((double) k / (k + 1))));
	}

	@Override
	public String toString() {
		return "Individual{" +
				"path=" + path +
				", cost=" + getCost() +
				", birth=" + birthTime +
				", death=" + deathTime +
				'}';
	}
}
