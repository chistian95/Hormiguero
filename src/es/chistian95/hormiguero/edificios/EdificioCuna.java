package es.chistian95.hormiguero.edificios;

import java.awt.Graphics2D;

public class EdificioCuna extends Edificio {
	private int bebes;
	
	public EdificioCuna(int x, int y) {
		this.x = x;
		this.y = y;
		this.bebes = 0;
		this.terminado = false;
	}
	
	public int getBebes() {
		return bebes;
	}
	
	public boolean tieneBebes() {
		return bebes > 0;
	}
	
	public boolean isLleno() {
		return bebes >= 10;
	}
	
	public void meterBebes() {
		bebes += 1;
	}
	
	public void sacarBebes() {
		bebes -= 1;
	}
	
	@Override
	public void render(Graphics2D g) {
		
	}
}
