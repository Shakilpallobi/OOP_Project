package CORE;

import MODE.Individual;

public interface Event_Strategy {
	void execute(Simulation_Context aContext, Individual aIndividual);
}
