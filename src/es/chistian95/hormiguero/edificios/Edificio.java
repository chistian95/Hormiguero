package es.chistian95.hormiguero.edificios;

import java.awt.Graphics2D;

public abstract class Edificio {
	protected int x;
	protected int y;
	protected boolean terminado;
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public abstract void render(Graphics2D g);
	
	public boolean isTerminado() {
		return terminado;
	}
	public void setTerminado(boolean terminado) {
		this.terminado = terminado;
	}
}
