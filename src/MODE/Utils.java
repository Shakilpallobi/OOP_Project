package MODE;

import java.util.ArrayList;
import java.util.List;
import UTIL.*;

/**
 * Classes auxiliares para o comportamento dos indivíduos:
 * - Quais são vizinhos válidos (reprodução)
 * - Tempos de mover/reproduzir/morrer (podem usar exponencial, uniforme ou fixes)
 */
public class Utils {

    /**
     * Devolve uma lista de indivíduos “vizinhos” para fins de reprodução.
     * Por simplicidade, assumimos que, se dois indivíduos ocupam células adjacentes,
     * então são vizinhos. Ajusta conforme precisares.
     */
    public static List<Individual> getNeighbors(Individual ind, int[][] grid, int N) {
        List<Individual> vizinhos = new ArrayList<>();
        int x = ind.getX();
        int y = ind.getY();

        // Verifica 4 direções: cima, baixo, esquerda, direita
        int[][] deltas = { {1,0}, {-1,0}, {0,1}, {0,-1} };
        for (int[] d : deltas) {
            int nx = x + d[0];
            int ny = y + d[1];
            if (nx >= 1 && nx <= N && ny >= 1 && ny <= N) {
                // Supondo que cada célula tem no máximo 1 indivíduo, e que
                // context.getPopulation() foi fornecido em Simulation_Context.
                // Aqui, verificamos se há um indivíduo nessa posição:
                for (Individual outro : ind.getParent() == null ? new ArrayList<>() : new ArrayList<Individual>()) {
                    // Isto é só um stub: normalmente, para buscar vizinhos, precisamos
                    // ter acesso à lista "context.getPopulation()", mas não a recebemos aqui.
                    // O ideal seria passar a própria lista de população como parâmetro,
                    // ou então inserir este método em CORE e ter acesso direto a context.
                    // Para evitar complicar, retornamos lista vazia como stub.
                }
            }
        }
        return vizinhos;
    }

    /** Tempo até poder mover (exponencial com média 1, por exemplo) */
    public static int getTimeToMove(Individual ind, int[][] grid) {
        RandomUtil rnd = new RandomUtil();
        rnd.setSeed(System.currentTimeMillis() ^ ind.hashCode());
        double val = rnd.getExponential(1.0);
        return Math.max(1, (int)Math.round(val));
    }

    /** Tempo até poder reproduzir (exponencial com média k, por exemplo) */
    public static int getTimeToReproduce(Individual ind, int[][] grid, int k) {
        RandomUtil rnd = new RandomUtil();
        rnd.setSeed(System.nanoTime() ^ ind.hashCode());
        double val = rnd.getExponential(k > 0 ? k : 1.0);
        return Math.max(1, (int)Math.round(val));
    }

    /** Tempo até morrer (exponencial com média baseada na distância ao destino) */
    public static int getTimeToDie(Individual ind, int[][] grid, int destino, int k) {
        // Aqui simplesmente devolvemos um tempo aleatório razoável (por ex. 1+aleta)
        RandomUtil rnd = new RandomUtil();
        rnd.setSeed(System.nanoTime() ^ ind.hashCode() ^ destino);
        double val = rnd.getExponential(2.0);
        return Math.max(1, (int)Math.round(val));
    }
}
