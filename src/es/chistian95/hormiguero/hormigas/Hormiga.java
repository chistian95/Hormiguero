package es.chistian95.hormiguero.hormigas;

import java.awt.Graphics2D;

import es.chistian95.hormiguero.Hormiguero;

public abstract class Hormiga {
	protected Hormiguero hormiguero;	
	protected int x;
	protected int y;
	
	public abstract void render(Graphics2D g);
	public abstract void tick();
}
