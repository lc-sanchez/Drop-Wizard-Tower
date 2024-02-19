package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Vida {
	private Image imagen;
	private int x;
	private int y;

	public Vida(int x, int y) {
		imagen = Herramientas.cargarImagen("Vida.gif");
		this.x = x;
		this.y = y;
	}

	public void dibujar(Entorno entorno) {
		entorno.dibujarImagen(imagen, this.x, this.y, 0, 0.15);
	}

}
