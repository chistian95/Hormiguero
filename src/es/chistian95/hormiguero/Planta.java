package es.chistian95.hormiguero;

import java.awt.Graphics2D;

import es.chistian95.hormiguero.utils.Utils;

public class Planta {		
	private int x;
	private int y;	
	
	private int etapa;
	
	public Planta(int x, int y) {
		this.x = x;
		this.y = y;
		
		this.etapa = 0;
	}
	
	public void tick() {
		if(this.etapa == 4) {
			return;
		}
		
		this.etapa += 1;
	}
	
	public boolean hasFruta() {
		return this.etapa == 4;
	}
	
	public void comer() {
		this.etapa = 0;
	}
	
	public void render(Graphics2D g) {
		g.setColor(Utils.hexToColor("#388E3C"));
		
		if(etapa >= 0) {
			pintar(g, x, y);
		}
		if(etapa >= 1) {
			pintar(g, x, y-1);
		}
		if(etapa >= 2) {
			pintar(g, x, y-2);
			pintar(g, x+1, y-3);
		}
		if(etapa >= 3) {
			pintar(g, x+2, y-3);
			pintar(g, x+3, y-2);
		}
		if(etapa >= 4) {
			g.setColor(Utils.hexToColor("#b71c1c"));
			
			pintar(g, x+3, y-1);
		}
	}
	
	private void pintar(Graphics2D g, int x, int y) {
		int tam = Hormiguero.TAM_CELDA;
		int dx = x * tam;
		int dy = y * tam;	
		
		g.fillRect(dx, dy, tam, tam);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
