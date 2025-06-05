package io;

import java.util.List;
import MODE.Coordenadas;
import MODE.Special_Cost_Zone;

public class SimulationParameters {
    private int n;                       // Número de linhas do grid
    private int m;                       // Número de colunas do grid
    private int initialPopulation;
    private int k;
    private int destino;
    private int totalTime;
    private int[][] grid;               // Poderás setar isto se parser preencher
    private List<Coordenadas> obstacles;
    private List<Special_Cost_Zone> costZones;

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getInitialPopulation() {
        return initialPopulation;
    }

    public void setInitialPopulation(int initialPopulation) {
        this.initialPopulation = initialPopulation;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getDestino() {
        return destino;
    }

    public void setDestino(int destino) {
        this.destino = destino;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public List<Coordenadas> getObstacles() {
        return obstacles;
    }

    public void setObstacles(List<Coordenadas> obstacles) {
        this.obstacles = obstacles;
    }

    public List<Special_Cost_Zone> getCostZones() {
        return costZones;
    }

    public void setCostZones(List<Special_Cost_Zone> costZones) {
        this.costZones = costZones;
    }
}
