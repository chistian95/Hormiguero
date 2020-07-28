package es.chistian95.hormiguero;

import java.awt.Graphics2D;

import es.chistian95.hormiguero.utils.OpenSimplex2F;

public class Hormiguero {
	public static final int ANCHO = 320;
	public static final int ALTO = 180;
	public static final int TAM_CELDA = Lanzador.ANCHO/ANCHO;
	public static final int ALTURA_HORMIGUERO = 80;
	
	public static final int ESCALA_TIERRA_G = 3;
	public static final int ESCALA_TIERRA_M = 2;
	public static final int ESCALA_TIERRA_P = 1;
	public static final double IMPORTANCIA_TIERRA_G = 1.0;
	public static final double IMPORTANCIA_TIERRA_M = 0.75; 
	public static final double IMPORTANCIA_TIERRA_P = 0.5; 	
	public static final double THRESHOLD_TIERRA = 0.5;
	
	public static final int ESCALA_PIEDRA_G = 8;
	public static final int ESCALA_PIEDRA_M = 6;
	public static final int ESCALA_PIEDRA_P = 4;
	public static final double IMPORTANCIA_PIEDRA_G = 0.5;
	public static final double IMPORTANCIA_PIEDRA_M = 0.45; 
	public static final double IMPORTANCIA_PIEDRA_P = 0.4; 	
	public static final double THRESHOLD_PIEDRA = 0.85;
	
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
		
		OpenSimplex2F noise = new OpenSimplex2F(this.seed);
		
		for(int x=0; x<ANCHO; x++) {
			for(int y=0; y<ALTO; y++) {
				double ruidoTierra = noise.noise2(x/ESCALA_TIERRA_G, y/ESCALA_TIERRA_G) * IMPORTANCIA_TIERRA_G;
				ruidoTierra += noise.noise2(x/ESCALA_TIERRA_M, y/ESCALA_TIERRA_M) * IMPORTANCIA_TIERRA_M;
				ruidoTierra += noise.noise2(x/ESCALA_TIERRA_P, y/ESCALA_TIERRA_P) * IMPORTANCIA_TIERRA_P;
				
				double ruidoPiedra = noise.noise2(x/ESCALA_PIEDRA_G, y/ESCALA_PIEDRA_G) * IMPORTANCIA_PIEDRA_G;
				ruidoPiedra += ruidoPiedra = noise.noise2(x/ESCALA_PIEDRA_M, y/ESCALA_PIEDRA_M) * IMPORTANCIA_PIEDRA_M;
				ruidoPiedra += ruidoPiedra = noise.noise2(x/ESCALA_PIEDRA_P, y/ESCALA_PIEDRA_P) * IMPORTANCIA_PIEDRA_P;
				
				if(x == 0 || y == 0 || x == ANCHO-1 || y == ALTO-1) {
					grid[x][y] = Casilla.PIEDRA;
				} else if(y < ALTURA_HORMIGUERO) {
					grid[x][y] = Casilla.AIRE;
				} else if(y == ALTURA_HORMIGUERO) {
					grid[x][y] = Casilla.HIERBA;
				} else if(y == ALTURA_HORMIGUERO+1 && noise.noise2(x, y) > 0) {
					grid[x][y] = Casilla.HIERBA;
				} else if(ruidoPiedra >= THRESHOLD_PIEDRA) {
					grid[x][y] = Casilla.PIEDRA;
				} else if(ruidoTierra >= THRESHOLD_TIERRA) {
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
