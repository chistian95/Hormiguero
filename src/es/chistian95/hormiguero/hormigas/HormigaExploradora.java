package es.chistian95.hormiguero.hormigas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Optional;

import es.chistian95.hormiguero.Casilla;
import es.chistian95.hormiguero.Hormiguero;
import es.chistian95.hormiguero.Planta;
import es.chistian95.hormiguero.edificios.Edificio;
import es.chistian95.hormiguero.edificios.EdificioAlmacen;
import es.chistian95.hormiguero.edificios.EdificioCuna;
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
		tickHormiga += 1;
		if(tickHormiga%Hormiguero.HAMBRE_HORMIGAS == 0) {
			hambre += 1;
		}
		
		if(objetivo == null || objetivo.size() == 0) {
			if(hambre >= 10 && edificio != null && edificio instanceof EdificioAlmacen) {
				EdificioAlmacen almacen = (EdificioAlmacen) edificio;
				
				if(almacen.tieneComida()) {
					almacen.sacarComida();
					
					hambre = Math.max(0, hambre-10);
				}
				
				edificio = null;
			} else if(comida && edificio != null && edificio instanceof EdificioAlmacen) {
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
			} else if(ponchando && edificio != null && edificio instanceof EdificioCuna) {
				EdificioCuna cuna = (EdificioCuna) edificio;
				
				if(!cuna.isLleno()) {
					ponchando = false;
					tickPonchar = 0;
					cuna.meterBebes();
				}
				
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
		if(hambre >= 10) {
			EdificioAlmacen target = null;
			
			for(EdificioAlmacen almacen : hormiguero.getAlmacenes()) {
				if(almacen.tieneComida()) {
					target = almacen;
					break;
				}
			}
			
			if(target != null) {
				int dx = target.getX();
				int dy = target.getY();
				
				edificio = target;
				objetivo = Finder.buscar(new int[] {this.x, this.y}, new int[] {dx, dy}, hormiguero.getGrid());
				
				return;
			}
		}
		
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
			Optional<Planta> target = hormiguero.getPlantas().stream().filter(p -> p.hasFruta()).sorted((a,b) -> {
				double distA = (a.getX()-this.x)*(a.getX()-this.x) + (a.getY()-this.y)*(a.getY()-this.y);
				double distB = (b.getX()-this.x)*(b.getX()-this.x) + (b.getY()-this.y)*(b.getY()-this.y);
				
				return Double.compare(distA, distB);
			}).findFirst();
			
			if(target.isPresent()) {
				int dx = target.get().getX();
				int dy = target.get().getY();
				
				planta = target.get();
				objetivo = Finder.buscar(new int[] {this.x, this.y}, new int[] {dx, dy}, hormiguero.getGrid());
				
				return;
			}
		}
		
		if(tickPonchar >= Hormiguero.GANAS_PONCHAR && !ponchando) {
			EdificioCuna target = null;
			for(EdificioCuna cuna : hormiguero.getCunas()) {
				if(cuna.isTerminado() && !cuna.isLleno()) {
					target = cuna;
					break;
				}
			}
			
			if(target != null) {
				int dx = target.getX();
				int dy = target.getY();
				
				ponchando = true;
				edificio = target;
				objetivo = Finder.buscar(new int[] {this.x, this.y}, new int[] {dx, dy}, hormiguero.getGrid());
			}
			
			return;
		}
		
		int dx = hormiguero.getCentroX();
		int dy = hormiguero.getCentroY();
		
		if(this.x == dx && this.y == dy) {
			tickPonchar += 1;
			return;
		}
		
		objetivo = Finder.buscar(new int[] {this.x, this.y}, new int[] {dx, dy}, hormiguero.getGrid());
	}
}
