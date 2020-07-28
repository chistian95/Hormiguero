package es.chistian95.hormiguero.utils.pathfinding;

public class Nodo {
	private int x;
	private int y;
	private int coste;
	private int costeSrc;
	private boolean visitado;
	private Nodo padre;
	
	public Nodo(int x, int y, int coste) {
		this.x = x;
		this.y = y;
		this.coste = coste;
		this.costeSrc = -1;
		this.visitado = false;
		this.padre = null;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCoste() {
		return coste;
	}

	public void setCoste(int coste) {
		this.coste = coste;
	}

	public int getCosteSrc() {
		return costeSrc;
	}

	public void setCosteSrc(int costeSrc) {
		this.costeSrc = costeSrc;
	}

	public boolean isVisitado() {
		return visitado;
	}

	public void setVisitado(boolean visitado) {
		this.visitado = visitado;
	}

	public Nodo getPadre() {
		return padre;
	}

	public void setPadre(Nodo padre) {
		this.padre = padre;
	}		
}
