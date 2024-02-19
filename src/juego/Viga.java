package juego;

import java.awt.Image;

import entorno.*;

public class Viga {
	
	private int x;
	private int y;
	private int ancho;
	private int alto;
	
	private Image imagen;

	public Viga(int x, int y) {
		this.x = x;
		this.y = y;
		this.ancho = 190;   
		this.alto = 1;   

		this.imagen = Herramientas.cargarImagen("viga.png");
	}
	public void dibujar(Entorno entorno){
		entorno.dibujarImagen(this.imagen, this.x, this.y, 0, 0.5);
	}
	
	public int getAncho() {
		return ancho;
	}
	public int getAlto() {
		return alto;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	

}
