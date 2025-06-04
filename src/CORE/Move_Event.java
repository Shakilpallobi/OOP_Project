package CORE;

import MODE.*;

import java.util.List;
import java.util.Random;

public class Move_Event implements Event_Strategy {

	private static final Random random = new Random();

	public Move_Event() {
		// Nada a fazer no construtor
	}

	@Override
	public void execute(Simulation_Context context, Individual individual) {
		Grid grid = context.getGrid();
		Coordenadas atual = individual.getLastPosition();

		// 1. Obter movimentos válidos
		List<Coordenadas> movimentos = grid.getValidMoves(atual);
		if (movimentos.isEmpty()) return; // sem movimento possível

		// 2. Escolher um movimento aleatório entre os válidos
		int index = random.nextInt(movimentos.size());
		Coordenadas novaPos = movimentos.get(index);

		// 3. Adicionar ao caminho do indivíduo
		individual.move(novaPos);

		// 4. Agendar próximo movimento (se ainda não morreu e tempo < τ)
		int tempoAtual = context.getTempoAtual();
		int tempoMorte = individual.getDeathTime();

		if (tempoAtual < tempoMorte && tempoAtual < context.getTempoFinal()) {
			int delta = gerarExponencial(context.getDelta(), individual, context);
			int novoTempo = tempoAtual + delta;

			if (novoTempo < tempoMorte && novoTempo <= context.getTempoFinal()) {
				Event novoMove = context.getEventFactory().createMoveEvent(individual, novoTempo);
				context.getPEC().addEvent(novoMove);
			}
		}
	}

	private int gerarExponencial(double mediaBase, Individual individuo, Simulation_Context ctx) {
		double comfort = individuo.getComfort(ctx.getGrid(), ctx.getDestino(), ctx.getK());
		double lambda = 1.0 / ((1 - Math.log(comfort)) * mediaBase);
		double r = Math.random();
		return (int) Math.ceil(-Math.log(1 - r) / lambda);
	}
}
