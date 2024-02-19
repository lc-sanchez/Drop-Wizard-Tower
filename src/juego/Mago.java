
package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.*;

public class Mago {

	private int x;
	private int y;
	private int ancho;
	private int alto;

	private int velocidadLateral;
	private int velocidadDeCaida;
	private double angulo;
	private double escala;

	private int cantVidas;
	private boolean conVida;
	private boolean sePuedeQuitarVida;

	private Image imagenIzq;
	private Image imagenDer;
	private boolean izquierda;

	private boolean hechizoDisponible;
	private int puntos;

	private int cargaPoderEspecial;
	private int limitePoderEspecial;

	public Mago(int x, int y) {
		this.ancho = 61;
		this.alto = 67;
		this.x = x;
		this.y = y;

		this.velocidadLateral = 1;
		this.velocidadDeCaida = 3;
		this.angulo = 0;
		this.escala = 0.15;

		this.cantVidas = 3;
		this.conVida = true;
		this.puntos = 0;

		this.imagenIzq = Herramientas.cargarImagen("Magoizq.gif");
		this.imagenDer = Herramientas.cargarImagen("Magodrch.gif");

		this.cargaPoderEspecial = 0;
		this.limitePoderEspecial = 800;

	}

	public void dibujar(Entorno entorno) {
		entorno.dibujarImagen((izquierda ? imagenIzq : imagenDer), x, y, angulo, escala);
	}

	public boolean chocoPared(Entorno e) {
		return this.x - this.ancho <= 33 || this.x + this.ancho >= e.ancho() - 28;
	}

	public void CambiarDireccion() {
		if (izquierda) {
			this.izquierda = false;
		} else {
			this.izquierda = true;
		}
	}

	public void mover() {
		if (this.conVida) {
			this.x += (izquierda ? this.velocidadLateral : -this.velocidadLateral);
		}
	}

	public void detenerse() {
		this.velocidadLateral = 0;
	}

	public void continuar() {
		this.velocidadLateral = 1;
	}

	public boolean teFuistePorHueco(Entorno entorno) {
		return (this.y >= entorno.alto());
	}

	public void aparecerArriba() {
		this.y = -5;
	}

	public void caer() {
		this.y += this.velocidadDeCaida;
	}

	public boolean estaEnViga(Viga viga) {
		if (this.x - this.ancho / 2 >= viga.getX() - viga.getAncho() / 2
				&& this.x - this.ancho / 2 <= viga.getX() + viga.getAncho() / 2 - 10) {
			
			if ((this.y + this.alto / 2 + 2 >= viga.getY() + viga.getAlto() / 2)
					&& (this.y + this.alto / 2 <= viga.getY() + viga.getAlto() / 2)) {
				return true;
			}
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

	public Hechizo disparar() {
		Herramientas.play("SonidoHechizo.wav");
		return new Hechizo(this.x, this.y, izquierda);
	}

	public void recargarHechizo() {
		if (this.conVida) {
			hechizoDisponible = true;
		}
	}

	public void deshabilitarHechizo() {
		hechizoDisponible = false;
	}

	public boolean tieneHechizoDisponible() {
		return hechizoDisponible;
	}

	public void dibujarPuntos(Entorno entorno) {
		entorno.cambiarFont("Showcard Gothic", 20, Color.white);
		entorno.escribirTexto("Puntos: " + this.puntos, entorno.ancho() - 180, 40);
	}

	public void sumarPuntos() {
		this.puntos += 100;
	}

	public void sumarPuntosCombo() {
		this.puntos += 500;
	}

	public void dibujarPuntosFin(Entorno entorno) {
		entorno.cambiarFont("Showcard Gothic", 40, Color.white);
		entorno.escribirTexto("Puntos:" + puntos, entorno.ancho() / 3 - 15, entorno.alto() - 120);
	}

	public void cargarPoderEspecial() {
		this.cargaPoderEspecial++;
	}

	public void dispararPoderEspecial(Hechizo[] hechizos) {
		this.recargarHechizo();
		for (int i = 1; i < hechizos.length; i++) {

			if (hechizos[i] == null && hechizos[i - 1] == null && this.tieneHechizoDisponible()) {
				hechizos[i] = this.disparar();
				this.CambiarDireccion();
				hechizos[i - 1] = disparar();
				this.CambiarDireccion();
				this.deshabilitarHechizo();
			}
		}

	}

	public boolean estaCargadoPoderEspecial() {
		if (this.cargaPoderEspecial > this.limitePoderEspecial) {
			return true;
		}
		return false;
	}

	public void reiniciarPoderEspecial() {
		this.cargaPoderEspecial = 0;
	}

	public void restarUnaVida() {
		cantVidas -= 1;
		this.puntos -= 200;
	}

	public boolean agarroItem(Diamante item) {
		return this.x + this.ancho / 2 >= item.getX() - item.getAncho() / 2
				&& this.x - this.ancho / 2 <= item.getX() + item.getAncho() / 2

				&& this.y - this.alto / 2 <= item.getY() + item.getAlto() / 2
				&& this.y + this.alto / 2 >= item.getY() - item.getAlto() / 2;
	}

	public void morir() {
		this.conVida = false;
		this.detenerse();
	}

	public void deshabilitarRestarVida() {
		sePuedeQuitarVida = false;
	}

	public void habilitarRestarVida() {
		sePuedeQuitarVida = true;
	}

	public boolean sePuedeRestarVida() {
		return sePuedeQuitarVida;
	}

	public boolean sigueVivo() {
		return conVida;
	}

	public int getPuntos() {
		return this.puntos;
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

	public boolean isIzquierda() {
		return izquierda;
	}

	public int getCantVidas() {
		return cantVidas;
	}

}
