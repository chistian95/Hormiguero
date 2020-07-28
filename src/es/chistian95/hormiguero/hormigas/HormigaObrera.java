package es.chistian95.hormiguero.hormigas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Random;

import es.chistian95.hormiguero.Casilla;
import es.chistian95.hormiguero.Hormiguero;
import es.chistian95.hormiguero.edificios.Edificio;
import es.chistian95.hormiguero.edificios.EdificioAlmacen;
import es.chistian95.hormiguero.edificios.EdificioCuna;
import es.chistian95.hormiguero.utils.Utils;
import es.chistian95.hormiguero.utils.pathfinding.Finder;

public class HormigaObrera extends Hormiga {
	private static Random rng = new Random();
	
	private List<Integer[]> objetivo;
	private Edificio edificio;
	
	public HormigaObrera(Hormiguero hormiguero, int x, int y) {
		this.hormiguero = hormiguero;
		this.x = x;
		this.y = y;
		objetivo = null;
	}

	@Override
	public void render(Graphics2D g) {
		g.setColor(Utils.hexToColor("#ffc107"));
		
		int dx = this.x * Hormiguero.TAM_CELDA;
		int dy = this.y * Hormiguero.TAM_CELDA;
		
		g.fillArc(dx, dy, Hormiguero.TAM_CELDA, Hormiguero.TAM_CELDA, 0, 365);
		
		g.setColor(Color.BLACK);
		g.drawArc(dx, dy, Hormiguero.TAM_CELDA, Hormiguero.TAM_CELDA, 0, 365);
	}

	@Override
	public void tick() {
		if(objetivo == null || objetivo.size() == 0) {
			if(edificio != null) {
				hormiguero.crearCueva(edificio.getX(), edificio.getY());
				edificio.setTerminado(true);
				edificio = null;
			}
			
			buscarObjetivo();
		} else {
			Integer[] dest = objetivo.remove(0);
			
			this.x = dest[0];
			this.y = dest[1];
			
			Casilla[][] grid = hormiguero.getGrid();
			
			if(grid[this.x][this.y].getPeso() >= 0 && grid[this.x][this.y] != Casilla.AIRE) {
				grid[this.x][this.y] = Casilla.CAMINO;
			}
		}
	}
	
	private void buscarObjetivo() {
		boolean hacerAlmacen = true;
		
		for(EdificioAlmacen almacen : hormiguero.getAlmacenes()) {
			if(!almacen.isLleno()) {
				hacerAlmacen = false;
				break;
			}
		}
		
		if(hacerAlmacen) {
			prepararAlmacen();
			return;
		}	
		
		boolean hacerCuna = true;
		
		for(EdificioCuna cuna : hormiguero.getCunas()) {
			if(!cuna.isLleno()) {
				hacerCuna = false;
				break;
			}
		}
		
		if(hacerCuna) {
			prepararCuna();
			return;
		}
		
		int dx = hormiguero.getCentroX();
		int dy = hormiguero.getCentroY();
		
		if(this.x == dx && this.y == dy) {
			return;
		}
		
		objetivo = Finder.buscar(new int[] {this.x, this.y}, new int[] {dx, dy}, hormiguero.getGrid());
	}
	
	private void prepararAlmacen() {
		edificio = null;
		while(edificio == null) {
			int altura = Hormiguero.ALTURA_HORMIGUERO + Hormiguero.TAM_CUEVAS*2;
			
			int dx = rng.nextInt(Hormiguero.ANCHO);
			int dy = rng.nextInt(Hormiguero.ALTO - altura) + altura;
			
			boolean libre = true;
			for(int px = dx-Hormiguero.TAM_CUEVAS; px < dx+Hormiguero.TAM_CUEVAS && libre; px++) {
				for(int py = dy-Hormiguero.TAM_CUEVAS; py < dy+Hormiguero.TAM_CUEVAS && libre; py++) {
					if(px < 0 || py < 0 || px >= Hormiguero.ANCHO || py >= Hormiguero.ALTO) {
						continue;
					}
					
					if(hormiguero.getGrid()[px][py] == Casilla.CAMINO) {
						libre = false;
						break;
					}
				}
			}
			
			if(libre) {
				edificio = new EdificioAlmacen(dx, dy);
				hormiguero.getAlmacenes().add((EdificioAlmacen) edificio);
				objetivo = Finder.buscar(new int[] {this.x, this.y}, new int[] {dx, dy}, hormiguero.getGrid());
			}
		}
	}
	
	private void prepararCuna() {
		edificio = null;
		while(edificio == null) {
			int altura = Hormiguero.ALTURA_HORMIGUERO + Hormiguero.TAM_CUEVAS*2;
			
			int dx = rng.nextInt(Hormiguero.ANCHO);
			int dy = rng.nextInt(Hormiguero.ALTO - altura) + altura;
			
			boolean libre = true;
			for(int px = dx-Hormiguero.TAM_CUEVAS; px < dx+Hormiguero.TAM_CUEVAS && libre; px++) {
				for(int py = dy-Hormiguero.TAM_CUEVAS; py < dy+Hormiguero.TAM_CUEVAS && libre; py++) {
					if(px < 0 || py < 0 || px >= Hormiguero.ANCHO || py >= Hormiguero.ALTO) {
						continue;
					}
					
					if(hormiguero.getGrid()[px][py] == Casilla.CAMINO) {
						libre = false;
						break;
					}
				}
			}
			
			if(libre) {
				edificio = new EdificioCuna(dx, dy);
				hormiguero.getCunas().add((EdificioCuna) edificio);
				objetivo = Finder.buscar(new int[] {this.x, this.y}, new int[] {dx, dy}, hormiguero.getGrid());
			}
		}
	}
}
