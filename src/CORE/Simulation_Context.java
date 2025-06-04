package CORE;

import MODE.*;
import io.*;

public class Simulation_Context {
	private Grid grid;
	private PEC pec;
	private PopulationManager population;
	private SimulationParameters parameters;
	private EventFactory eventFactory;
	private int tempoAtual;

	public Simulation_Context(Grid grid, PEC pec, PopulationManager population, SimulationParameters parameters) {
		this.grid = grid;
		this.pec = pec;
		this.population = population;
		this.parameters = parameters;
		this.eventFactory = new EventFactory();
		this.tempoAtual = 0;
	}
	public void removeIndividual(Individual individual) {
		// Remove o indivíduo da lista de população
		population.remove(individual);}

	public Grid getGrid() {
		return grid;
	}

	public PEC getPEC() {
		return pec;
	}

	public PopulationManager getPopulation() {
		return population;
	}

	public SimulationParameters getParameters() {
		return parameters;
	}

	public EventFactory getEventFactory() {
		return eventFactory;
	}

	public int getTempoAtual() {
		return tempoAtual;
	}

	public void setTempoAtual(int tempo) {
		this.tempoAtual = tempo;
	}

	public Coordenadas getDestino() {
		return parameters.getFinalPoint();
	}

	public int getK() {
		return parameters.getK();
	}

	public int getTempoFinal() {
		return parameters.getTau();
	}

	public double getMu() {
		return parameters.getMu();
	}

	public double getRho() {
		return parameters.getRho();
	}

	public double getDelta() {
		return parameters.getDelta();
	}

	/**
	 * Melhor indivíduo: se alguém chegou ao fim, devolve o de menor custo;
	 * caso contrário, devolve o de maior conforto.
	 */
	public Individual getBestFitIndividual() {
		Coordenadas destino = getDestino();
		var all = population.getALL();

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

		if (melhorComDestino != null) return melhorComDestino;

		// Caso ninguém tenha chegado ao destino, retorna o com maior conforto
		return all.stream()
				.max((a, b) -> Double.compare(
						a.getComfort(grid, destino, getK()),
						b.getComfort(grid, destino, getK())))
				.orElse(null);
	}
}
