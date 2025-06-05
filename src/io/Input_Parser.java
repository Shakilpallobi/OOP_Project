package io;

import MODE.Coordenadas;
import MODE.Special_Cost_Zone;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Input_Parser {

	public Input_Parser() {
		// Construtor vazio
	}

	/**
	 * Lê parâmetros a partir de um ficheiro no formato “chave=valor” (text/plain).
	 * Exemplos de chaves que reconhecemos: n, m, initialPopulation, k, destino, totalTime.
	 * Zonas especiais e obstáculos deverão vir definidos num bloco adicional (dar parse manual).
	 */
	public SimulationParameters parse(String filePath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line;
		SimulationParameters params = new SimulationParameters();

		// Valores temporários para grid, obstáculos e zonas:
		int[][] grid = null;
		List<Coordenadas> obstacles = new ArrayList<>();
		List<Special_Cost_Zone> costZones = new ArrayList<>();

		while ((line = reader.readLine()) != null) {
			line = line.trim();
			if (line.isEmpty() || line.startsWith("#")) continue;
			String[] parts = line.split("=");
			if (parts.length != 2) continue;
			String key = parts[0].trim();
			String value = parts[1].trim();
			switch (key) {
				case "n":
					params.setN(Integer.parseInt(value));
					break;
				case "m":
					params.setM(Integer.parseInt(value));
					break;
				case "initialPopulation":
					params.setInitialPopulation(Integer.parseInt(value));
					break;
				case "k":
					params.setK(Integer.parseInt(value));
					break;
				case "destino":
					params.setDestino(Integer.parseInt(value));
					break;
				case "totalTime":
					params.setTotalTime(Integer.parseInt(value));
					break;
				// Aqui podes adicionar lógica para ler “grid”, “obstacle”, “costZone” conforme o formato real
				default:
					// Se forem linhas do tipo “obstacle=3,5” ou “costZone=1,1,3,3,5” (x1,y1,x2,y2,custo)
					if (key.equals("gridCell")) {
						// Exemplo de armazenar no grid, mas é só stub
					} else if (key.equals("obstacle")) {
						String[] coords = value.split(",");
						int ox = Integer.parseInt(coords[0].trim());
						int oy = Integer.parseInt(coords[1].trim());
						obstacles.add(new Coordenadas(ox, oy));
					} else if (key.equals("costZone")) {
						String[] vals = value.split(",");
						int x1 = Integer.parseInt(vals[0].trim());
						int y1 = Integer.parseInt(vals[1].trim());
						int x2 = Integer.parseInt(vals[2].trim());
						int y2 = Integer.parseInt(vals[3].trim());
						int custo = Integer.parseInt(vals[4].trim());
						costZones.add(new Special_Cost_Zone(new Coordenadas(x1, y1),
								new Coordenadas(x2, y2),
								custo));
					}
					break;
			}
		}
		reader.close();

		params.setObstacles(obstacles);
		params.setCostZones(costZones);
		return params;
	}

	/**
	 * Chama parse(String) externamente, só para alinhar com Main.java.
	 */
	public SimulationParameters parseFile(String filePath) throws IOException {
		return parse(filePath);
	}

	/**
	 * Lê parâmetros da consola (System.in) no mesmo formato “chave=valor”.
	 * Basta usar: parser.parse(System.in).
	 */
	public SimulationParameters parse(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		SimulationParameters params = new SimulationParameters();

		int[][] grid = null;
		List<Coordenadas> obstacles = new ArrayList<>();
		List<Special_Cost_Zone> costZones = new ArrayList<>();

		while ((line = reader.readLine()) != null) {
			line = line.trim();
			if (line.isEmpty() || line.startsWith("#")) continue;
			String[] parts = line.split("=");
			if (parts.length != 2) continue;
			String key = parts[0].trim();
			String value = parts[1].trim();
			switch (key) {
				case "n":
					params.setN(Integer.parseInt(value));
					break;
				case "m":
					params.setM(Integer.parseInt(value));
					break;
				case "initialPopulation":
					params.setInitialPopulation(Integer.parseInt(value));
					break;
				case "k":
					params.setK(Integer.parseInt(value));
					break;
				case "destino":
					params.setDestino(Integer.parseInt(value));
					break;
				case "totalTime":
					params.setTotalTime(Integer.parseInt(value));
					break;
				default:
					if (key.equals("obstacle")) {
						String[] coords = value.split(",");
						int ox = Integer.parseInt(coords[0].trim());
						int oy = Integer.parseInt(coords[1].trim());
						obstacles.add(new Coordenadas(ox, oy));
					} else if (key.equals("costZone")) {
						String[] vals = value.split(",");
						int x1 = Integer.parseInt(vals[0].trim());
						int y1 = Integer.parseInt(vals[1].trim());
						int x2 = Integer.parseInt(vals[2].trim());
						int y2 = Integer.parseInt(vals[3].trim());
						int custo = Integer.parseInt(vals[4].trim());
						costZones.add(new Special_Cost_Zone(new Coordenadas(x1, y1),
								new Coordenadas(x2, y2),
								custo));
					}
					break;
			}
		}

		params.setObstacles(obstacles);
		params.setCostZones(costZones);
		return params;
	}
}
