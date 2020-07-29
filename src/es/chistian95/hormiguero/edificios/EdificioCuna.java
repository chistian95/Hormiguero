package es.chistian95.hormiguero.edificios;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import es.chistian95.hormiguero.Hormiguero;
import es.chistian95.hormiguero.hormigas.HormigaExploradora;
import es.chistian95.hormiguero.hormigas.HormigaObrera;
import es.chistian95.hormiguero.utils.Utils;

public class EdificioCuna extends Edificio {
	private static Random rng = new Random();
	
	private List<Huevo> huevos;
	
	public EdificioCuna(Hormiguero hormiguero, int x, int y) {
		this.hormiguero = hormiguero;
		this.x = x;
		this.y = y;
		this.huevos = new ArrayList<Huevo>();
		this.terminado = false;
	}
	
	public int getBebes() {
		return huevos.size();
	}
	
	public boolean tieneBebes() {
		return huevos.size() > 0;
	}
	
	public boolean isLleno() {
		return huevos.size() >= 10;
	}
	
	public void meterBebe() {
		huevos.add(new Huevo());
	}
	
	public void tick() {
		Iterator<Huevo> itHuevos = huevos.iterator();
		while(itHuevos.hasNext()) {
			Huevo huevo = itHuevos.next();
			huevo.tick();
			
			if(huevo.getTick() >= Hormiguero.VELOCIDAD_HUEVOS) {
				itHuevos.remove();
				
				if(rng.nextDouble() < Hormiguero.RATE_EXPLORADORAS) {
					this.hormiguero.getHormigas().add(new HormigaExploradora(this.hormiguero, this.x, this.y));
				} else {
					this.hormiguero.getHormigas().add(new HormigaObrera(this.hormiguero, this.x, this.y));
				}
			}
		}
	} 
	
	@Override
	public void render(Graphics2D g) {
		int radio = Hormiguero.TAM_CUEVAS;
		
		int nBebes = this.huevos.size();
		int ancho = (int) Math.round(Hormiguero.TAM_CELDA);
		
		int dx = this.x*Hormiguero.TAM_CELDA - nBebes/2*ancho;
		int dy = this.y*Hormiguero.TAM_CELDA + radio;		
		
		g.setColor(Utils.hexToColor("#00c853"));
		
		for(int i=0; i<nBebes; i++) {
			g.fillArc(dx+i*ancho, dy, ancho, ancho, 0, 365);
		}
	}
}
