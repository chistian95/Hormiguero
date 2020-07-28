package es.chistian95.hormiguero;

import java.awt.Graphics2D;

import es.chistian95.hormiguero.utils.OpenSimplex2F;

public class Hormiguero {
	public static final int ANCHO = 320;
	public static final int ALTO = 180;
	public static final int TAM_CELDA = Lanzador.ANCHO/ANCHO;
	
	public static final int ESCALA_NOISE_G = 3;
	public static final int ESCALA_NOISE_M = 2;
	public static final int ESCALA_NOISE_P = 1;
	public static final double IMPORTANCIA_NOISE_G = 1.0;
	public static final double IMPORTANCIA_NOISE_M = 0.75; 
	public static final double IMPORTANCIA_NOISE_P = 0.5; 
	public static final double THRESHOLD_NOISE = 0.5;
	
	private Casilla[][] grid;
	private long seed;
	
	public Hormiguero(long seed) {
		this.seed = seed;
		
		generarMundo();
	}
	
	public void render(Graphics2D g) {
		for(int x=0; x<ANCHO; x++) {
			for(int y=0; y<ALTO; y++) {
				Casilla cas = grid[x][y];
				
				int dx = x*TAM_CELDA;
				int dy = y*TAM_CELDA;
				
				cas.render(g, dx, dy, TAM_CELDA);
			}
		}
	}
	
	public void update() {
		
	}
	
	private void generarMundo() {
		grid = new Casilla[ANCHO][ALTO];		
		
		OpenSimplex2F noiseG = new OpenSimplex2F(this.seed);
		OpenSimplex2F noiseM = new OpenSimplex2F(this.seed);
		OpenSimplex2F noiseP = new OpenSimplex2F(this.seed);
		
		for(int x=0; x<ANCHO; x++) {
			for(int y=0; y<ALTO; y++) {
				double ruido = noiseG.noise2(x/ESCALA_NOISE_G, y/ESCALA_NOISE_G) * IMPORTANCIA_NOISE_G;
				ruido += noiseM.noise2(x/ESCALA_NOISE_M, y/ESCALA_NOISE_M) * IMPORTANCIA_NOISE_M;
				ruido += noiseP.noise2(x/ESCALA_NOISE_P, y/ESCALA_NOISE_P) * IMPORTANCIA_NOISE_P;
				
				if(x == 0 || y == 0 || x == ANCHO-1 || y == ALTO-1) {
					grid[x][y] = Casilla.PIEDRA;
				} else if(ruido >= THRESHOLD_NOISE) {
					grid[x][y] = Casilla.TIERRA_SECA;
				} else {
					grid[x][y] = Casilla.TIERRA;
				}
			}
		}
	}
	
	public Casilla[][] getGrid() {
		return grid;
	}
}
