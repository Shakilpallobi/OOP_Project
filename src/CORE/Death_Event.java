package CORE;
import MODE.*;


public class Death_Event implements Event_Strategy {

	public Death_Event() {
		// Nenhuma configuração necessária para já
	}

	@Override
	public void execute(Simulation_Context context, Individual individual) {
		// Remover o indivíduo da simulação
		context.removeIndividual(individual);
		// Opcional: logging ou debug
		 System.out.println("Indivíduo morreu: " + individual);
	}
}
