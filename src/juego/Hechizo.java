package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Hechizo {
	private int x;
	private int y;
	private int ancho;
	
	private int velocidad;
	boolean izquierda;
	
	private Image imagen;
	
	public Hechizo (int x, int y, boolean izquierda){
		this.x = x;
		this.y = y;
		this.ancho = 30;
		this.velocidad = 5;
		this.izquierda = izquierda;
		this.imagen = Herramientas.cargarImagen("Hechizo.gif");
		
	}
	
	public void dibujar (Entorno entorno){
		entorno.dibujarImagen(imagen, x, y,0, 1);
	}
	public void mover() {
		this.x += (izquierda ? this.velocidad : -this.velocidad);
	}
	
	public boolean chocasteBorde (Entorno entorno , int anchoPared){ 
		return (this.x - this.ancho /2  <= anchoPared ||
				this.x + this.ancho /2 >= entorno.ancho() - anchoPared);
	}


	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getAncho() {
		return ancho;
	}

	
}
