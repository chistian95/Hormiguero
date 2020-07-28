package es.chistian95.hormiguero.hormigas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import es.chistian95.hormiguero.Casilla;
import es.chistian95.hormiguero.Hormiguero;
import es.chistian95.hormiguero.Planta;
import es.chistian95.hormiguero.edificios.Edificio;
import es.chistian95.hormiguero.edificios.EdificioAlmacen;
import es.chistian95.hormiguero.utils.Utils;
import es.chistian95.hormiguero.utils.pathfinding.Finder;

public class HormigaExploradora extends Hormiga {	
	private List<Integer[]> objetivo;
	private Edificio edificio;
	private Planta planta;
	
	private boolean comida;
	
	public HormigaExploradora(Hormiguero hormiguero, int x, int y) {
		this.hormiguero = hormiguero;
		this.x = x;
		this.y = y;
		objetivo = null;
		comida = false;
	}

	@Override
	public void render(Graphics2D g) {
		g.setColor(Utils.hexToColor("#9c27b0"));
		
		int dx = this.x * Hormiguero.TAM_CELDA;
		int dy = this.y * Hormiguero.TAM_CELDA;
		
		g.fillArc(dx, dy, Hormiguero.TAM_CELDA, Hormiguero.TAM_CELDA, 0, 365);
		
		g.setColor(Color.BLACK);
		g.drawArc(dx, dy, Hormiguero.TAM_CELDA, Hormiguero.TAM_CELDA, 0, 365);
	}

	@Override
	public void tick() {
		if(objetivo == null || objetivo.size() == 0) {
			if(comida && edificio != null && edificio instanceof EdificioAlmacen) {
				EdificioAlmacen almacen = (EdificioAlmacen) edificio;
				
				if(!almacen.isLleno()) {
					comida = false;
					almacen.meterComida();
				}
				
				edificio = null;
			} else if(!comida && planta != null) {
				if(planta.hasFruta()) {
					planta.comer();
					comida = true;
				}
				
				planta = null;
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
		if(comida) {
			EdificioAlmacen target = null;
			
			for(EdificioAlmacen almacen : hormiguero.getAlmacenes()) {
				if(almacen.isTerminado() && !almacen.isLleno()) {
					target = almacen;
					break;
				}
			}
			
			if(target != null) {
				int dx = target.getX();
				int dy = target.getY();
				
				edificio = target;
				objetivo = Finder.buscar(new int[] {this.x, this.y}, new int[] {dx, dy}, hormiguero.getGrid());
			}
			
			return;
		}
		
		if(!comida) {
			Planta target = null;
			
			for(Planta planta : hormiguero.getPlantas()) {
				if(planta.hasFruta()) {
					target = planta;
					break;
				}
			}
			
			if(target != null) {
				int dx = target.getX();
				int dy = target.getY();
				
				planta = target;
				objetivo = Finder.buscar(new int[] {this.x, this.y}, new int[] {dx, dy}, hormiguero.getGrid());
				
				return;
			}
		}
		
		int dx = hormiguero.getCentroX();
		int dy = hormiguero.getCentroY();
		
		if(this.x == dx && this.y == dy) {
			return;
		}
		
		objetivo = Finder.buscar(new int[] {this.x, this.y}, new int[] {dx, dy}, hormiguero.getGrid());
	}
}
