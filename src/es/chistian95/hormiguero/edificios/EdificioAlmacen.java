package es.chistian95.hormiguero.edificios;

import java.awt.Graphics2D;

import es.chistian95.hormiguero.Hormiguero;
import es.chistian95.hormiguero.utils.Utils;

public class EdificioAlmacen extends Edificio {
	private int comida;
	
	public EdificioAlmacen(Hormiguero hormiguero, int x, int y) {
		this.hormiguero = hormiguero;
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
		int radio = Hormiguero.TAM_CUEVAS;
		
		int nComida = this.comida;
		int ancho = (int) Math.round(Hormiguero.TAM_CELDA);
		
		int dx = this.x*Hormiguero.TAM_CELDA - this.comida/2*ancho;
		int dy = this.y*Hormiguero.TAM_CELDA + radio;		
		
		g.setColor(Utils.hexToColor("#b71c1c"));
		
		for(int i=0; i<nComida; i++) {
			g.fillArc(dx+i*ancho, dy, ancho, ancho, 0, 365);
		}
	}
}
