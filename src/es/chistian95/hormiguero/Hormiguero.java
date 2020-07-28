package es.chistian95.hormiguero;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.chistian95.hormiguero.edificios.EdificioAlmacen;
import es.chistian95.hormiguero.edificios.EdificioCuna;
import es.chistian95.hormiguero.hormigas.Hormiga;
import es.chistian95.hormiguero.hormigas.HormigaExploradora;
import es.chistian95.hormiguero.hormigas.HormigaObrera;
import es.chistian95.hormiguero.utils.OpenSimplex2F;

public class Hormiguero {
	public static final int ANCHO = 320;
	public static final int ALTO = 180;
	public static final int TAM_CELDA = Lanzador.ANCHO/ANCHO;
	public static final int ALTURA_HORMIGUERO = 20;
	public static final int TAM_CUEVAS = 6;
	
	public static final int VELOCIDAD_HORMIGAS = 2;
	public static final int HAMBRE_HORMIGAS = 200;
	public static final int GANAS_PONCHAR = 800;
	public static final int VELOCIDAD_HUEVOS = 5000;
	public static final double RATE_EXPLORADORAS = 0.8;
	
	public static final double THRESHOLD_PLANTAS = 0.85;
	public static final double TICKRATE_PLANTAS = 0.002;
	
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
	
	private Random rng;
	private Casilla[][] grid;
	private List<Planta> plantas;
	private List<Hormiga> hormigas;
	private long seed;
	OpenSimplex2F noise;
	private long tickJuego;
	
	private int centroX;
	private int centroY;
	
	private List<EdificioAlmacen> almacenes;
	private List<EdificioCuna> cunas;
	
	public Hormiguero(long seed) {
		this.seed = seed;
		rng = new Random(seed);
		
		generarMundo();
		
		hormigas = new ArrayList<Hormiga>();
		
		hormigas.add(new HormigaObrera(this, centroX, centroY));
		hormigas.add(new HormigaExploradora(this, centroX, centroY));
		
		almacenes = new ArrayList<EdificioAlmacen>();
		cunas = new ArrayList<EdificioCuna>();
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
		
		for(Planta planta : plantas) {
			planta.render(g);
		}
		
		for(EdificioAlmacen edificio : almacenes) {
			edificio.render(g);
		}
		
		for(EdificioCuna edificio : cunas) {
			edificio.render(g);
		}
		
		for(Hormiga hormiga : hormigas) {
			hormiga.render(g);
		}
	}
	
	public void update() {		
		tickJuego += 1;
		
		for(Planta planta : plantas) {
			if(rng.nextDouble() < TICKRATE_PLANTAS) {
				planta.tick();
			}
		}
		
		if(tickJuego % VELOCIDAD_HORMIGAS == 0) {
			for(Hormiga hormiga : hormigas) {
				hormiga.tick();
			}
		}
		
		if(tickJuego % VELOCIDAD_HUEVOS == 0) {
			for(EdificioCuna cuna : cunas) {
				if(cuna.tieneBebes()) {
					cuna.sacarBebes();
					
					if(rng.nextDouble() < RATE_EXPLORADORAS) {
						hormigas.add(new HormigaExploradora(this, cuna.getX(), cuna.getY()));
					} else {
						hormigas.add(new HormigaObrera(this, cuna.getX(), cuna.getY()));
					}
				}
			}
		}
	}
	
	private void generarMundo() {
		grid = new Casilla[ANCHO][ALTO];		
		
		noise = new OpenSimplex2F(this.seed);
		
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
		
		plantas = new ArrayList<Planta>();
		
		for(int x=0; x<ANCHO; x++) {
			int y = ALTURA_HORMIGUERO-1;
			
			if(noise.noise2(x, y) > THRESHOLD_PLANTAS) {
				plantas.add(new Planta(x, y));
			}
		}
		
		centroX = ANCHO/2;
		centroY = (ALTO-ALTURA_HORMIGUERO)/2 + ALTURA_HORMIGUERO;
		
		crearCueva(centroX, centroY);
	}
	
	public void crearCueva(int x, int y) {
		int radio = TAM_CUEVAS;
		
		for(int dy=-radio; dy<=radio; dy++) {
			for(int dx=-radio; dx<=radio; dx++) {
				if(dx*dx + dy*dy <= radio*radio) {
					if(dx+x < 0 || dy+y < 0 || dx+x >= ANCHO || dy+y >= ALTO) {
						continue;
					}
					grid[dx+x][dy+y] = Casilla.CAMINO;
				}
			}
		}
	}
	
	public Casilla[][] getGrid() {
		return grid;
	}
	
	public long getSeed() {
		return seed;
	}
	
	public int getCentroX() {
		return centroX;
	}
	
	public int getCentroY() {
		return centroY;
	}
	
	public List<Planta> getPlantas() {
		return plantas;
	}
	
	public List<EdificioAlmacen> getAlmacenes() {
		return almacenes;
	}
	
	public List<EdificioCuna> getCunas() {
		return cunas;
	}
}
