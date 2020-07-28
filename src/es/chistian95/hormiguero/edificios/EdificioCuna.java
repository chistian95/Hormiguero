package es.chistian95.hormiguero.edificios;

import java.awt.Graphics2D;

import es.chistian95.hormiguero.Hormiguero;
import es.chistian95.hormiguero.utils.Utils;

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
		int radio = Hormiguero.TAM_CUEVAS;
		
		int nBebes = this.bebes;
		int ancho = (int) Math.round(Hormiguero.TAM_CELDA);
		
		int dx = this.x*Hormiguero.TAM_CELDA - nBebes/2*ancho;
		int dy = this.y*Hormiguero.TAM_CELDA + radio;		
		
		g.setColor(Utils.hexToColor("#00c853"));
		
		for(int i=0; i<nBebes; i++) {
			g.fillArc(dx+i*ancho, dy, ancho, ancho, 0, 365);
		}
	}
}
