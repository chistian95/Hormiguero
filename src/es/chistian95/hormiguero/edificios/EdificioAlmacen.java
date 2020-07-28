package es.chistian95.hormiguero.edificios;

import java.awt.Graphics2D;

public class EdificioAlmacen extends Edificio {
	private int comida;
	
	public EdificioAlmacen(int x, int y) {
		this.x = x;
		this.y = y;
		this.comida = 0;
		this.terminado = false;
	}
	
	public int getComida() {
		return comida;
	}
	
	public boolean tieneComida() {
		return comida > 0;
	}
	
	public boolean isLleno() {
		return comida >= 10;
	}
	
	public void meterComida() {
		comida += 1;
	}
	
	public void sacarComida() {
		comida -= 1;
	}

	@Override
	public void render(Graphics2D g) {
		
	}
}
