package es.chistian95.hormiguero;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Lanzador implements Runnable {
	public static final int ANCHO = 1280;
	public static final int ALTO = 720;
	
	final long FPS = 60;
	final long FPS_DELTA = 1000/FPS;
	
	JFrame frame;
	Canvas canvas;
	BufferStrategy bfs;
	
	public Lanzador() {		
		frame = new JFrame("Hormiguero");
		
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setPreferredSize(new Dimension(ANCHO, ALTO));
		panel.setLayout(null);
		
		canvas = new Canvas();
		canvas.setBounds(0, 0, ANCHO, ALTO);
		canvas.setIgnoreRepaint(true);
		
		panel.add(canvas);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setUndecorated(true);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		canvas.createBufferStrategy(4);
		bfs = canvas.getBufferStrategy();
		
		canvas.requestFocus();
	}
	
	public void run() {
		while(true) {
			long loopInicio = System.currentTimeMillis();
			
			update();
			render();
			
			long loopFinal = System.currentTimeMillis();
			long loopDelta = loopFinal - loopInicio;
			
			if(loopDelta < FPS_DELTA) {
				try {
					Thread.sleep((FPS_DELTA - loopDelta));
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void render() {		
		Graphics2D g = (Graphics2D) bfs.getDrawGraphics();
		g.clearRect(0, 0, ANCHO, ALTO);
		render(g);
		g.dispose();
		bfs.show();
	}
	
	protected void render(Graphics2D g) {
		
	}
	
	protected void update() {		
		
	}
	
	public static void main(String[] args) {
		Lanzador ex = new Lanzador();
		new Thread(ex).start();
	}
}
