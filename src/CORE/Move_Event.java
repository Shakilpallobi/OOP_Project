package CORE;

import MODE.Individual;

/**
 * Evento que move um indivíduo para a célula adjacente de maior conforto.
 */
public class Move_Event implements Event_Strategy {

	@Override
	public void execute(Simulation_Context context, Individual individual) {
		// Obtém a matriz de terreno (grid) diretamente como int[][]
		int[][] grid = context.getGrid();

		// Posição atual
		int x = individual.getX();
		int y = individual.getY();
		int bestX = x;
		int bestY = y;

		// Conforto na célula atual
		double bestComfort = individual.getComfort(grid, context.getDestino(), context.getK());

		// Explora todas as 8 células adjacentes (incluindo diagonais)
		for (int dx = -1; dx <= 1; dx++) {
			for (int dy = -1; dy <= 1; dy++) {
				if (dx == 0 && dy == 0) continue;

				int newX = x + dx;
				int newY = y + dy;

				// Verifica se (newX,newY) está dentro do grid (1..N)
				if (newX >= 1 && newX <= context.getN() && newY >= 1 && newY <= context.getN()) {
					// Move temporariamente para (newX,newY)
					individual.setX(newX);
					individual.setY(newY);

					// Calcula conforto nessa posição
					double comfort = individual.getComfort(grid, context.getDestino(), context.getK());
					if (comfort > bestComfort) {
						bestComfort = comfort;
						bestX = newX;
						bestY = newY;
					}

					// Reverte para a posição original antes de testar outra direção
					individual.setX(x);
					individual.setY(y);
				}
			}
		}

		// Depois de testar todas as adjacências, move de facto para a melhor posição
		individual.setX(bestX);
		individual.setY(bestY);
	}
}
