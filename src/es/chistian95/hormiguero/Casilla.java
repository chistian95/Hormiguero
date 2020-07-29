package es.chistian95.hormiguero;

import java.awt.Graphics2D;

import es.chistian95.hormiguero.utils.Utils;
import es.chistian95.hormiguero.utils.Utils.Consumer4;

public enum Casilla {
	TIERRA(5, (g, x, y, ancho) -> {
		g.setColor(Utils.hexToColor("#795548"));
		
		g.fillRect(x, y, ancho, ancho);
	}),
	TIERRA_SECA(10, (g, x, y, ancho) -> {
		g.setColor(Utils.hexToColor("#5D4037"));
		
		g.fillRect(x, y, ancho, ancho);
	}),
	PIEDRA(-1, (g, x, y, ancho) -> {
		g.setColor(Utils.hexToColor("#757575"));
		
		g.fillRect(x, y, ancho, ancho);
	}),
	CAMINO(2, (g, x, y, ancho) -> {
		g.setColor(Utils.hexToColor("#D7CCC8"));
		
		g.fillRect(x, y, ancho, ancho);
	}),
	AIRE(2, (g, x, y, ancho) -> {
		g.setColor(Utils.hexToColor("#2196F3"));
		
		g.fillRect(x, y, ancho, ancho);
	}),
	HIERBA(200, (g, x, y, ancho) -> {
		g.setColor(Utils.hexToColor("#4CAF50"));
		
		g.fillRect(x, y, ancho, ancho);
	});
	
	private int peso;
	private Consumer4<Graphics2D, Integer, Integer, Integer> renderf;
	
	private Casilla(int peso, Consumer4<Graphics2D, Integer, Integer, Integer> renderf) {
		this.peso = peso;
		this.renderf = renderf;
	}
	
	public int getPeso() {
		return peso;
	}
	
	public void render(Graphics2D g, int x, int y, int ancho) {
		this.renderf.apply(g, x, y, ancho);
	}
}
