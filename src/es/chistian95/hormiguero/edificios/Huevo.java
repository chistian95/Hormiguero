package es.chistian95.hormiguero.edificios;

public class Huevo {
	private long tick;
	
	public Huevo() {
		this.tick = 0;
	}
	
	public void tick() {
		tick += 1;
	}
	
	public long getTick() {
		return tick;
	}
}
