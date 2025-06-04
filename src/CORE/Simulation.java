package CORE;
import MODE.*;
public class Simulation {
	private Simulation_Context context;
	private double currentTime;
	private int eventCount = 0;
	private int observationIndex = 0;
	private double nextObservationTime;

	public Simulation(Simulation_Context context) {
		this.context = context;
		this.currentTime = 0;
		this.nextObservationTime = 0; // começa em 0
	}

	public void run() {
		PEC pec = context.getPEC();
		int tau = context.getTempoFinal();

		observe(); // observação inicial no tempo 0

		while (pec.hasEvents() && currentTime <= tau) {
			Event event = pec.getNextEvent();
			if (event.getTime() > tau) break;

			currentTime = event.getTime();
			context.setTempoAtual((int) currentTime);

			processEvent(event);
			eventCount++;

			// Verifica se é hora de observar
			if (currentTime >= nextObservationTime && observationIndex <= 20) {
				observe();
				observationIndex++;
				nextObservationTime = (tau / 20.0) * observationIndex;
			}
		}

		printMelhorIndividuoFinal();
	}

	private void processEvent(Event event) {
		event.execute(context);
	}

	private void observe() {
		System.out.println("Observation number: " + observationIndex);
		System.out.println("Present time: " + (int) currentTime);
		System.out.println("Number of realized events: " + eventCount);

		var vivos = context.getPopulation().getALIVE();
		System.out.println("Population size: " + vivos.size());

		Individual melhor = context.getBestFitIndividual();
		boolean chegouAoFim = melhor.getLastPosition().equals(context.getDestino());

		System.out.println("Final point has been hit: " + (chegouAoFim ? "yes" : "no"));
		System.out.println("Path of the best fit individual: " + melhor.getPath());

		if (chegouAoFim) {
			System.out.println("Cost/Comfort: " + melhor.getCost(context.getGrid()));
		} else {
			System.out.println("Cost/Comfort: " + melhor.getComfort(context.getGrid(), context.getDestino(), context.getK()));
		}

		System.out.println();
	}

	private void printMelhorIndividuoFinal() {
		Individual melhor = context.getBestFitIndividual();
		boolean chegouAoFim = melhor.getLastPosition().equals(context.getDestino());

		if (chegouAoFim) {
			System.out.println("Best fit individual: " + melhor.getPath() + " with cost " + melhor.getCost(context.getGrid()));
		} else {
			System.out.println("Best fit individual: " + melhor.getPath() + " with comfort " + melhor.getComfort(context.getGrid(), context.getDestino(), context.getK()));
		}
	}
}
