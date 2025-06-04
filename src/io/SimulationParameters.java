package io;

import MODE.*;

import java.util.List;

public class SimulationParameters {
    private int n;
    private int m;
    private Coordenadas start;
    private Coordenadas goal;
    private int tau;
    private int nu;
    private int nuMax;
    private int k;
    private double mu;
    private double delta;
    private double rho;

    private List<Special_Cost_Zone> costZones;
    private List<Coordenadas> obstacles;

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

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public Coordenadas getStartPoint() {
        return start;
    }

    public Coordenadas getFinalPoint() {
        return goal;
    }

    public List<Special_Cost_Zone> getCostZones() {
        return costZones;
    }

    public List<Coordenadas> getObstacles() {
        return obstacles;
    }

    public int getTau() {
        return tau;
    }

    public int getNu() {
        return nu;
    }

    public int getNuMax() {
        return nuMax;
    }

    public int getK() {
        return k;
    }

    public double getMu() {
        return mu;
    }

    public double getDelta() {
        return delta;
    }

    public double getRho() {
        return rho;
    }

    /**
     * Cria uma instância da grelha com obstáculos e zonas de custo.
     */
    public Grid buildGrid() {
        Grid g = new Grid(n, m);

        for (Coordenadas obs : obstacles) {
            g.addObstacle(obs);
        }

        for (Special_Cost_Zone scz : costZones) {
            g.addSpecialCostZone(scz);
        }

        return g;
    }
}
