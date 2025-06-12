package io;

import MODE.*; // Assuming MODE package contains Grid, Coordenadas, Special_Cost_Zone

import java.util.List;

/**
 * A data class that encapsulates all the parameters required to configure and run a simulation.
 * These parameters define the grid environment, starting and ending points,
 * special cost zones, obstacles, and various simulation constants related to event timing and population dynamics.
 */
public class SimulationParameters {
    /** The number of rows in the simulation grid. */
    private int n;
    /** The number of columns in the simulation grid. */
    private int m;
    /** The starting {@link Coordenadas} for individuals in the simulation. */
    private Coordenadas start;
    /** The target or goal {@link Coordenadas} for individuals. */
    private Coordenadas goal;
    /** The total duration of the simulation (tau). */
    private int tau;
    /** The initial number of individuals in the simulation (nu). */
    private int nu;
    /** The maximum number of individuals allowed in the simulation (nuMax). */
    private int nuMax;
    /** The 'k' parameter, a weighting factor used in comfort calculations. */
    private int k;
    /** The mean time for death events (mu), used in exponential distribution. */
    private double mu;
    /** The mean time for movement events (delta), used in exponential distribution. */
    private double delta;
    /** The mean time for reproduction events (rho), used in exponential distribution. */
    private double rho;

    /** A list of {@link Special_Cost_Zone} objects defining areas with elevated movement costs. */
    private List<Special_Cost_Zone> costZones;
    /** A list of {@link Coordenadas} representing obstacles on the grid. */
    private List<Coordenadas> obstacles;

    /**
     * Constructs a new SimulationParameters object with all required simulation settings.
     *
     * @param n The number of rows in the grid.
     * @param m The number of columns in the grid.
     * @param start The starting coordinates for individuals.
     * @param goal The target/goal coordinates.
     * @param costZones A list of special cost zones.
     * @param obstacles A list of obstacle coordinates.
     * @param tau The total simulation time.
     * @param nu The initial population size.
     * @param nuMax The maximum allowed population size.
     * @param k The comfort weighting factor.
     * @param mu The mean for death event timing.
     * @param delta The mean for movement event timing.
     * @param rho The mean for reproduction event timing.
     */
    public SimulationParameters(
            int n, int m,
            Coordenadas start, Coordenadas goal,
            List<Special_Cost_Zone> costZones, List<Coordenadas> obstacles,
            int tau, int nu, int nuMax, int k,
            double mu, double delta, double rho
    ) {
        this.n = n;
        this.m = m;
        this.start = start;
        this.goal = goal;
        this.costZones = costZones;
        this.obstacles = obstacles;
        this.tau = tau;
        this.nu = nu;
        this.nuMax = nuMax;
        this.k = k;
        this.mu = mu;
        this.delta = delta;
        this.rho = rho;
    }

    /**
     * Returns the number of rows in the simulation grid.
     * @return The number of rows.
     */
    public int getN() { return n; }
    /**
     * Returns the number of columns in the simulation grid.
     * @return The number of columns.
     */
    public int getM() { return m; }
    /**
     * Returns the starting {@link Coordenadas} for individuals.
     * @return The start point.
     */
    public Coordenadas getStartPoint() { return start; }
    /**
     * Returns the target/goal {@link Coordenadas} for individuals.
     * @return The end point (goal).
     */
    public Coordenadas getEndPoint() { return goal; }
    /**
     * Returns the list of {@link Special_Cost_Zone} objects defined for the grid.
     * @return A list of special cost zones.
     */
    public List<Special_Cost_Zone> getSpecialCostZones() { return costZones; }
    /**
     * Returns the list of {@link Coordenadas} representing obstacles on the grid.
     * @return A list of obstacle coordinates.
     */
    public List<Coordenadas> getObstacles() { return obstacles; }
    /**
     * Returns the total duration of the simulation (tau).
     * @return The total simulation time.
     */
    public int getTau() { return tau; }
    /**
     * Returns the initial number of individuals in the simulation (nu).
     * @return The initial population size.
     */
    public int getNu() { return nu; }
    /**
     * Returns the maximum number of individuals allowed in the simulation (nuMax).
     * @return The maximum population size.
     */
    public int getNuMax() { return nuMax; }
    /**
     * Returns the 'k' parameter, a weighting factor used in comfort calculations.
     * @return The comfort weighting factor.
     */
    public int getK() { return k; }
    /**
     * Returns the mean time for death events (mu).
     * @return The mean death time.
     */
    public double getMu() { return mu; }
    /**
     * Returns the mean time for movement events (delta).
     * @return The mean movement time.
     */
    public double getDelta() { return delta; }
    /**
     * Returns the mean time for reproduction events (rho).
     * @return The mean reproduction time.
     */
    public double getRho() { return rho; }

    /**
     * Provides an alternative getter for the goal coordinates,
     * ensuring compatibility with other parts of the codebase that might expect {@code getDestino()}.
     * @return The target/goal {@link Coordenadas}.
     */
    public Coordenadas getDestino() { return goal; }
    /**
     * Provides an alternative getter for the goal coordinates,
     * ensuring compatibility with other parts of the codebase that might expect {@code getGoal()}.
     * @return The target/goal {@link Coordenadas}.
     */
    public Coordenadas getGoal() { return goal; }
    /**
     * Provides an alternative getter for the list of special cost zones,
     * ensuring compatibility with other parts of the codebase that might expect {@code getCostZones()}.
     * @return A list of special cost zones.
     */
    public List<Special_Cost_Zone> getCostZones() { return costZones; }

    /**
     * Constructs and returns a {@link Grid} object based on the grid dimensions,
     * obstacles, and special cost zones defined in these simulation parameters.
     *
     * @return A fully configured {@link Grid} instance ready for simulation.
     */
    public Grid buildGrid() {
        Grid g = new Grid(n, m);
        // Add all obstacles to the grid
        for (Coordenadas obs : obstacles) g.addObstacle(obs);
        // Add all special cost zones to the grid
        for (Special_Cost_Zone scz : costZones) g.addSpecialCostZone(scz);
        return g;
    }
}