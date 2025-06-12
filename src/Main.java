import CORE.*;
import io.*;
import MODE.*;

/**
 * The main entry point for the simulation application.
 * This class handles command-line argument parsing to determine the simulation
 * parameters (either from a file or randomly generated), initializes the
 * simulation environment (grid, event queue, population manager), creates
 * the initial population and their corresponding events, and then
 * starts the simulation run.
 */
public class Main {
    /**
     * Constructs a new Main instance.
     * This is a default constructor; no specific initialization is required.
     */
    public Main() {
        // Default constructor
    }

    /**
     * The main method of the application. It parses command-line arguments,
     * sets up the simulation, and runs it.
     *
     * <p>Command-line arguments can be in two formats:</p>
     * <ul>
     * <li>{@code -f <filepath>}: Reads simulation parameters from the specified file.</li>
     * <li>{@code -r <values...>}: Generates random simulation parameters based on the provided values.</li>
     * </ul>
     *
     * @param args Command-line arguments passed to the application.
     * Expected formats:
     * <ul>
     * <li>{@code -f input.txt}</li>
     * <li>{@code -r n m xi yi xf yf nscz nobst tau nu nuMax k mu delta rho}</li>
     * </ul>
     */
    public static void main(String[] args) {
        try {
            Input_Parser parser = new Input_Parser();
            SimulationParameters params;

            // Check command-line arguments to determine input mode
            if (args.length > 0 && args[0].equals("-f")) {
                // Example: java -jar projeto.jar -f input.txt
                if (args.length < 2) {
                    System.err.println("Erro: O modo -f requer o caminho do ficheiro. Usa -f <ficheiro>");
                    return;
                }
                String path = args[1];
                params = parser.parseFile(path);

            } else if (args.length > 0 && args[0].equals("-r")) {
                // Example: java -jar projeto.jar -r 5 5 1 1 5 5 1 4 100 10 100 3 10 1 1
                // The parseArgs method itself will validate the number of arguments for -r mode
                params = parser.parseArgs(args);

            } else {
                System.err.println("Erro: argumentos inválidos. Usa -f <ficheiro> ou -r <valores>");
                return;
            }

            // Create the grid based on parsed parameters
            Grid grid = params.buildGrid();

            // Initialize core simulation structures:
            // PEC (Priority Event Calendar) to manage events chronologically
            PEC pec = new PEC();
            // PopulationManager to manage individuals in the simulation
            // It's initialized with null context initially and then updated
            // because Simulation_Context needs PopulationManager, and vice-versa (circular dependency).
            PopulationManager pm = new PopulationManager(null);
            // Simulation_Context holds all shared simulation state and parameters
            Simulation_Context context = new Simulation_Context(grid, pec, pm, params);
            // Re-initialize PopulationManager with the correct context
            pm = new PopulationManager(context);
            // Update the Simulation_Context with the correctly initialized PopulationManager
            context = new Simulation_Context(grid, pec, pm, params);

            // Get the EventFactory from the context for creating events
            EventFactory factory = context.getEventFactory();

            // Create initial population and schedule their initial events
            // 'nu' represents the initial number of individuals
            for (int i = 0; i < params.getNu(); i++) {
                Individual ind = new Individual(params.getStartPoint(), 0, 0); // New individual starting at start point
                // Set individual's death time based on a random exponential distribution (lambda = mu)
                ind.setDeathTime((int) (Math.ceil(-Math.log(1 - Math.random()) * params.getMu())));
                pm.add(ind); // Add individual to the population manager

                // Schedule initial events for each individual
                pec.addEvent(factory.createDeathEvent(ind, ind.getDeathTime()));
                // Schedule initial move event based on random exponential distribution (lambda = delta)
                pec.addEvent(factory.createMoveEvent(ind, (int) (Math.ceil(-Math.log(1 - Math.random()) * params.getDelta()))));
                // Schedule initial reproduction event based on random exponential distribution (lambda = rho)
                pec.addEvent(factory.createReproductionEvent(ind, (int) (Math.ceil(-Math.log(1 - Math.random()) * params.getRho()))));
            }

            // Create and run the main simulation loop
            Simulation sim = new Simulation(context);
            sim.run();

        } catch (Exception e) {
            // Catch any exceptions during parsing or simulation and print stack trace
            System.err.println("An error occurred during simulation execution:");
            e.printStackTrace();
        }
    }
}