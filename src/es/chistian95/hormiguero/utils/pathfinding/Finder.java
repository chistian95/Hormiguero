package es.chistian95.hormiguero.utils.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import es.chistian95.hormiguero.Casilla;
import es.chistian95.hormiguero.Hormiguero;

public class Finder {
	public static List<Integer[]> buscar(int[] src, int[] dest, Casilla[][] mapa) {
		List<Integer[]> camino = new ArrayList<Integer[]>();
		
		Nodo[][] grid = casillaToNodo(mapa);		
		Queue<Nodo> q = new PriorityQueue<Nodo>((a,b) -> {
			return Integer.compare(a.getCosteSrc(), b.getCosteSrc());
		});
		
		q.add(grid[src[0]][src[1]]);
		
		while(q.size() > 0) {
			Nodo pt = q.poll();
			int x = pt.getX();
			int y = pt.getY();
			pt.setVisitado(true);
			
			if(dest[0] == x && dest[1] == y) {
				Nodo nodo = pt;
				
				while(nodo.getPadre() != null) {					
					camino.add(new Integer[] {nodo.getX(), nodo.getY()});					
					nodo = nodo.getPadre();
				}
				
				Collections.reverse(camino);
				return camino;
			}
			
			if((x - 1 >= 0) && y < Hormiguero.ALTO && !grid[x-1][y].isVisitado() && grid[x-1][y].getCoste() >= 0) {
				int nuevoCoste = grid[x][y].getCosteSrc() + grid[x-1][y].getCoste();
				if(nuevoCoste < grid[x-1][y].getCosteSrc() || grid[x-1][y].getCosteSrc() < 0) {
					grid[x-1][y].setCosteSrc(nuevoCoste);
					grid[x-1][y].setPadre(grid[x][y]);
					
					q.add(grid[x-1][y]);
				}
			}
			if((x + 1 < Hormiguero.ANCHO) && y < Hormiguero.ALTO && !grid[x+1][y].isVisitado() && grid[x+1][y].getCoste() >= 0) {
				int nuevoCoste = grid[x][y].getCosteSrc() + grid[x+1][y].getCoste();
				if(nuevoCoste < grid[x+1][y].getCosteSrc() || grid[x+1][y].getCosteSrc() < 0) {
					grid[x+1][y].setCosteSrc(nuevoCoste);
					grid[x+1][y].setPadre(grid[x][y]);
					
					q.add(grid[x+1][y]);
				}
			}
			if((y - 1 > Hormiguero.ALTURA_HORMIGUERO-2) && x < Hormiguero.ANCHO - 1 && !grid[x][y-1].isVisitado() && grid[x][y-1].getCoste() >= 0) {
				int nuevoCoste = grid[x][y].getCosteSrc() + grid[x][y-1].getCoste();
				if(nuevoCoste < grid[x][y-1].getCosteSrc() || grid[x][y-1].getCosteSrc() < 0) {
					grid[x][y-1].setCosteSrc(nuevoCoste);
					grid[x][y-1].setPadre(grid[x][y]);
					
					q.add(grid[x][y-1]);
				}
			}
			if((y + 1 < Hormiguero.ALTO) && x < Hormiguero.ANCHO && !grid[x][y+1].isVisitado() && grid[x][y+1].getCoste() >= 0) {
				int nuevoCoste = grid[x][y].getCosteSrc() + grid[x][y+1].getCoste();
				if(nuevoCoste < grid[x][y+1].getCosteSrc() || grid[x][y+1].getCosteSrc() < 0) {
					grid[x][y+1].setCosteSrc(nuevoCoste);
					grid[x][y+1].setPadre(grid[x][y]);
					
					q.add(grid[x][y+1]);
				}
			}
		}
	 
		return camino;
	}
	
	private static Nodo[][] casillaToNodo(Casilla[][] mapa) {
		int ancho = Hormiguero.ANCHO;
		int alto = Hormiguero.ALTO;
		
		Nodo[][] grid = new Nodo[ancho][alto];
		
		for(int x=0,lenX=ancho; x<lenX; x++) {
			for(int y=0,lenY=alto; y<lenY; y++) {
				grid[x][y] = new Nodo(x, y, mapa[x][y].getPeso());
			}
		}
		
		return grid;
	}
}
