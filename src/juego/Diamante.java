package juego;

import java.awt.Image;
import entorno.*;

public class Diamante {

	private int x;
	private int y;

	private int ancho;
	private int alto;

	private Image imagenDiamante;

	public Diamante(int x, int y) {
		this.x = x;
		this.y = y;
		this.ancho = 25;
		this.alto = 25;
		this.imagenDiamante = Herramientas.cargarImagen("Diamante.gif");
	}

	public void dibujar(Entorno entorno) {
		entorno.dibujarImagen(imagenDiamante, this.x, this.y, 0, 0.15);
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
