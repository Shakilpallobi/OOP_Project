package io;

import MODE.*; // Assuming this imports Coordenadas, Grid, Special_Cost_Zone

import java.io.*;
import java.util.*;

/**
 * A utility class responsible for parsing simulation parameters from different sources,
 * primarily from a text file or from command-line arguments (for random generation).
 * It translates raw input into a structured {@link SimulationParameters} object.
 */
public class Input_Parser {

	/**
	 * Constructs a new Input_Parser.
	 * This constructor requires no special initialization.
	 */
	public Input_Parser() {
		// Construtor vazio
	}

	/**
	 * Reads and parses simulation parameters from a specified file path.
	 * The file is expected to follow a specific format:
	 * The first line contains main simulation parameters (grid dimensions, start/end points,
	 * counts for special zones/obstacles, and simulation constants).
	 * Subsequent sections (if Nscz or Nobst are greater than 0) detail special cost zones
	 * and obstacle coordinates.
	 *
	 * @param filePath The path to the input file containing simulation parameters.
	 * @return A {@link SimulationParameters} object populated with the parsed values.
	 * @throws IOException If an I/O error occurs while reading the file, or if the file format is invalid.
	 * @throws NumberFormatException If any numeric value in the file cannot be parsed.
	 */
	public SimulationParameters parseFile(String filePath) throws IOException {
		BufferedReader br = null; // Declare br outside the try block for finally
		try {
			br = new BufferedReader(new FileReader(filePath));
			String line = br.readLine();

			// First line: main parameters
			String[] parts = line.trim().split("\\s+");
			int n = Integer.parseInt(parts[0]);
			int m = Integer.parseInt(parts[1]);
			int xi = Integer.parseInt(parts[2]);
			int yi = Integer.parseInt(parts[3]);
			int xf = Integer.parseInt(parts[4]);
			int yf = Integer.parseInt(parts[5]);
			int nscz = Integer.parseInt(parts[6]);
			int nobst = Integer.parseInt(parts[7]);
			int tau = Integer.parseInt(parts[8]);
			int nu = Integer.parseInt(parts[9]);
			int nuMax = Integer.parseInt(parts[10]);
			int k = Integer.parseInt(parts[11]);
			double mu = Double.parseDouble(parts[12]);
			double delta = Double.parseDouble(parts[13]);
			double rho = Double.parseDouble(parts[14]);

			List<Special_Cost_Zone> zonas = new ArrayList<>();
			if (nscz > 0) {
				br.readLine(); // Consume the "special cost zones:" header line
				for (int i = 0; i < nscz; i++) {
					String[] z = br.readLine().trim().split("\\s+");
					Coordenadas inf = new Coordenadas(Integer.parseInt(z[0]), Integer.parseInt(z[1]));
					Coordenadas sup = new Coordenadas(Integer.parseInt(z[2]), Integer.parseInt(z[3]));
					int custo = Integer.parseInt(z[4]);
					zonas.add(new Special_Cost_Zone(inf, sup, custo));
				}
			}

			List<Coordenadas> obstaculos = new ArrayList<>();
			if (nobst > 0) {
				br.readLine(); // Consume the "obstacles:" header line
				for (int i = 0; i < nobst; i++) {
					String[] o = br.readLine().trim().split("\\s+");
					obstaculos.add(new Coordenadas(Integer.parseInt(o[0]), Integer.parseInt(o[1])));
				}
			}

			return new SimulationParameters(n, m,
					new Coordenadas(xi, yi),
					new Coordenadas(xf, yf),
					zonas, obstaculos,
					tau, nu, nuMax, k, mu, delta, rho);

		} finally {
			if (br != null) {
				br.close(); // Ensure the reader is closed even if an error occurs
			}
		}
	}

	/**
	 * Parses simulation parameters provided as command-line arguments in "random" mode.
	 * This mode is indicated by the first argument being "-r". The subsequent 15 arguments
	 * represent the main simulation parameters. Special cost zones and obstacles are
	 * generated randomly based on the provided counts (nscz, nobst) and grid dimensions.
	 *
	 * @param args An array of strings representing the command-line arguments.
	 * Expected format: `"-r" n m xi yi xf yf nscz nobst tau nu nuMax k mu delta rho`
	 * @return A {@link SimulationParameters} object populated with the parsed and randomly generated values.
	 * @throws IllegalArgumentException If the command-line arguments do not match the expected format or count.
	 * @throws NumberFormatException If any argument expected to be a number cannot be parsed.
	 */
	public SimulationParameters parseArgs(String[] args) {
		// 16 = "-r" + 15 expected values
		if (!args[0].equals("-r") || args.length != 16) {
			throw new IllegalArgumentException("Formato inválido para o modo -r: usa 15 valores após -r. Ex: -r n m xi yi xf yf nscz nobst tau nu nuMax k mu delta rho");
		}

		int n      = Integer.parseInt(args[1]);
		int m      = Integer.parseInt(args[2]);
		int xi     = Integer.parseInt(args[3]);
		int yi     = Integer.parseInt(args[4]);
		int xf     = Integer.parseInt(args[5]);
		int yf     = Integer.parseInt(args[6]);
		int nscz   = Integer.parseInt(args[7]);
		int nobst  = Integer.parseInt(args[8]);
		int tau    = Integer.parseInt(args[9]);
		int nu     = Integer.parseInt(args[10]);
		int nuMax  = Integer.parseInt(args[11]);
		int k      = Integer.parseInt(args[12]);
		double mu     = Double.parseDouble(args[13]);
		double delta  = Double.parseDouble(args[14]);
		double rho    = Double.parseDouble(args[15]);

		Random rand = new Random();
		List<Special_Cost_Zone> zonas = new ArrayList<>();
		List<Coordenadas> obstaculos = new ArrayList<>();

		// Generate random special cost zones
		for (int i = 0; i < nscz; i++) {
			int x1 = 1 + rand.nextInt(n);
			int y1 = 1 + rand.nextInt(m);
			// Ensure x2 >= x1 and y2 >= y1
			int x2 = x1 + rand.nextInt(Math.max(1, n - x1 + 1)); // +1 to allow x2=n
			int y2 = y1 + rand.nextInt(Math.max(1, m - y1 + 1)); // +1 to allow y2=m
			int cost = 2 + rand.nextInt(4); // cost between 2 and 5 (inclusive)
			zonas.add(new Special_Cost_Zone(
					new Coordenadas(x1, y1),
					new Coordenadas(x2, y2),
					cost));
		}

		// Generate random obstacles
		for (int i = 0; i < nobst; i++) {
			obstaculos.add(new Coordenadas(
					1 + rand.nextInt(n), // x from 1 to n
					1 + rand.nextInt(m))); // y from 1 to m
		}

		return new SimulationParameters(n, m,
				new Coordenadas(xi, yi),
				new Coordenadas(xf, yf),
				zonas, obstaculos,
				tau, nu, nuMax, k, mu, delta, rho);
	}
}