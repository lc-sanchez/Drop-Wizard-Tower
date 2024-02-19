package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Monster {

	private int x;
	private int y;

	private int ancho;
	private int alto;

	private int velocidadLateral;
	private int velocidadCaida;
	private boolean izquierda;

	private Image imagenIzq;
	private Image imagenDer;
	private Image imagenDerCongelado;
	private Image imagenIzqCongelado;
	private double angulo;

	private int tiempoEnElQueSeCongelo;
	private int tiempoQuePuedeEstarCongelado;
	private boolean congelado;
	private boolean congeladoYchocado;
	private boolean chocadoPorOtroMonstruo;

	public Monster(int x, int y) {
		this.x = x;
		this.y = y;
		this.ancho = 40;
		this.alto = 67;
		this.angulo = 0;

		this.congeladoYchocado = false;
		this.velocidadLateral = 1;
		this.velocidadCaida = 3;
		this.congelado = false;
		this.tiempoQuePuedeEstarCongelado = 500;
		this.tiempoEnElQueSeCongelo = 0;
		this.chocadoPorOtroMonstruo = false;

		this.imagenIzq = Herramientas.cargarImagen("MonstruoIzq.gif");
		this.imagenDer = Herramientas.cargarImagen("MonstruoDer.gif");
		this.imagenDerCongelado = Herramientas.cargarImagen("MonstruoCongeladoDer.gif");
		this.imagenIzqCongelado = Herramientas.cargarImagen("MonstruoCongeladoIzq.gif");
	}

	public void dibujar(Entorno entorno) {
		entorno.dibujarImagen((izquierda ? imagenIzq : imagenDer), x, y, 0, 0.15);
	}

	public boolean estaEnViga(Viga viga) {
		if (this.x - this.ancho / 2 >= viga.getX() - viga.getAncho() / 2
				&& this.x - this.ancho / 2 <= viga.getX() + viga.getAncho() / 2 - 10
				
				&& this.y + this.alto / 2 + 2 >= viga.getY() + viga.getAlto() / 2
				&& this.y + this.alto / 2 <= viga.getY() + viga.getAlto() / 2) {
			return true;
		}
		return false;
	}

	public boolean estaEnAlgunaViga(Viga[] vigas) {
		for (int i = 0; i < vigas.length; i++) {
			if (estaEnViga(vigas[i])) {
				return true;
			}
		}
		return false;
	}

	public void mover() {
		this.x += (izquierda ? this.velocidadLateral : -this.velocidadLateral);
	}

	public void caer() {
		this.y += this.velocidadCaida;
	}

	public void cambiarDireccion() {
		if (izquierda) {
			this.izquierda = false;
		} else {
			this.izquierda = true;
		}
	}

	public void detenerse() {
		this.velocidadLateral = 0;
	}

	public void continuar() {
		this.velocidadLateral = 1;
	}

	public boolean chocoPared(Entorno e) {
		return this.x - this.ancho <= 40 || this.x + this.ancho >= e.ancho() - 45;
	}

	public boolean teFuistePorHueco(Entorno entorno) {
		return (this.y >= entorno.alto());
	}

	public void aparecerArriba() {
		this.y = -1;
	}

	public boolean alcanzadoPorHechizo(Hechizo hechizo) {
		return hechizo.getX() + hechizo.getAncho() / 2 >= this.x - this.ancho / 2
				&& hechizo.getX() - hechizo.getAncho() / 2 <= this.x - this.ancho / 2
				&& hechizo.getY() + hechizo.getAncho() <= this.y + this.alto / 2
				&& hechizo.getY() - hechizo.getAncho() >= this.y - this.alto / 2;
	}

	public void congelate(Entorno entorno) {
		this.congelado = true;
		entorno.dibujarImagen((izquierda ? imagenDerCongelado : imagenIzqCongelado), x, y, angulo, 0.15);
	}

	public boolean estaCongelado() {
		if (this.tiempoEnElQueSeCongelo >= this.tiempoQuePuedeEstarCongelado 
				&& !this.chocadoPorOtroMonstruo) {
			this.congelado = false;
			tiempoEnElQueSeCongelo = 0;
			this.continuar();
		}
		if (this.congelado) {
			tiempoEnElQueSeCongelo++;
		}
		return this.congelado;
	}

	public void fueChocadoYcongelado() {
		this.congeladoYchocado = true;
	}

	public boolean fueCongeladoYchocado() {
		return congeladoYchocado;
	}

	public boolean loChocoMago(Mago mago) {
		if (mago.getX() + mago.getAncho() / 2 >= this.x - this.ancho / 2
				&& mago.getX() - mago.getAncho() / 2 <= this.x + this.ancho / 2
				
				&& mago.getY() - mago.getAlto() / 2 <= this.y + this.alto / 2
				&& mago.getY() + mago.getAlto() / 2 >= this.y - this.alto / 2) {
			return true;
		}
		return false;
	}

	public boolean loChocoMounstruoCongelado(Monster[] monsters) {
		for (Monster h : monsters) {
			if (h.fueCongeladoYchocado() && h.getX() + h.getAncho() / 2 >= this.x - this.ancho / 2
					&& h.getX() - h.getAncho() / 2 <= this.x + this.ancho / 2
					
					&& h.getY() - h.getAlto() / 2 <= this.y + this.alto / 2
					&& h.getY() + h.getAlto() / 2 >= this.y - this.alto / 2) {
				return true;
			}
		}
		return false;
	}

	public void chocadoPorMonstruo() {
		this.chocadoPorOtroMonstruo = true;
	}

	public boolean monstruoFueChocadoPorMonstruo() {
		return this.chocadoPorOtroMonstruo;
	}

	public void rodarPorElSuelo() {
		this.velocidadLateral = 4;
		this.velocidadCaida = 3;

		if (this.izquierda) {
			this.angulo -= Math.PI;
		} else {
			this.angulo += Math.PI;
		}
	}

	public boolean MonsterMuerto(Entorno entorno) {
		if (this.y > entorno.alto() + 5) {
			return true;
		}
		return false;
	}

	public int TiempoQuePuedeEstarCongelado() {
		return tiempoQuePuedeEstarCongelado;
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

	public int getAlto() {
		return alto;
	}

}
