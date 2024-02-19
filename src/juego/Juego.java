package juego;

import java.awt.Color;
import java.awt.Image;
import javax.sound.sampled.Clip;
import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {

	private Entorno entorno;

	private int anchoPANTALLA;
	private int altoPANTALLA;
	private Image fondo;
	private int anchoPared;
	private int anchoItem;
	
	private Clip sonidoJuego;
	
	private Mago mago;

	private Viga[] vigas;
	private Monster[] monsters;
	private Hechizo[] hechizos;
	private Vida[] vidas;
	private Diamante[] items;
	
	private int MonstersMuertos;
	private Image FinJuego;
	private Image Ganaste;


	public Juego() {

		this.anchoPANTALLA = 600;
		this.altoPANTALLA = 700;

		this.entorno = new Entorno(this, "Juegazo ", anchoPANTALLA, altoPANTALLA);

		fondo = Herramientas.cargarImagen("fondo.png");
		sonidoJuego = Herramientas.cargarSonido("SonidoFondo.wav");
		FinJuego = Herramientas.cargarImagen("Gameover.jpg");
		Ganaste = Herramientas.cargarImagen("Ganador.jpg");
		this.anchoPared = 62;
		this.anchoItem = 50;
		this.MonstersMuertos = 0;

		vigas = new Viga[8];
		monsters = new Monster[4];
		hechizos = new Hechizo[1000];
		vidas = new Vida[3];
		items = new Diamante[5];

		vigas[0] = new Viga(entorno.ancho() / 2, entorno.alto() / 6);
		vigas[1] = new Viga(anchoPared + entorno.ancho() / 9, entorno.alto() / 3);
		vigas[2] = new Viga(entorno.ancho() - entorno.ancho() / 4 - entorno.alto() / 50, entorno.alto() / 3);
		vigas[3] = new Viga(entorno.ancho() / 2, entorno.alto() / 2 + entorno.alto() / 50);
		vigas[4] = new Viga(entorno.ancho() - entorno.ancho() / 4 - anchoPared / 4,
				entorno.alto() - entorno.alto() / 50);
		vigas[5] = new Viga(entorno.ancho() / 9 + anchoPared, entorno.alto() - entorno.alto() / 50);
		vigas[6] = new Viga(entorno.ancho() / 2 - entorno.ancho() / 9, entorno.alto() / 2 + entorno.alto() / 4);
		vigas[7] = new Viga(entorno.ancho() / 2 + anchoPared + 8, entorno.alto() / 2 + entorno.alto() / 4);

		monsters[0] = new Monster(150, 650);
		monsters[1] = new Monster(500, 200);
		monsters[2] = new Monster(150, 200);
		monsters[3] = new Monster(500, 650);

		vidas[0] = new Vida(anchoPared - anchoPared / 2, anchoPared / 2);
		vidas[1] = new Vida(anchoPared - anchoPared / 2, anchoPared / 2 * 3);
		vidas[2] = new Vida(anchoPared - anchoPared / 2, anchoPared / 2 * 5);

		items[0] = new Diamante(anchoPared + anchoItem, entorno.alto() / 6 + anchoItem);
		items[1] = new Diamante(entorno.ancho() - anchoPared - anchoItem, entorno.alto() / 6 + anchoItem);
		items[2] = new Diamante(entorno.ancho() / 2, entorno.alto() / 2 + entorno.alto() / 50 - anchoItem);
		items[3] = new Diamante(entorno.ancho() - entorno.ancho() / 4 - anchoPared / 4 - anchoItem,
				entorno.alto() - entorno.alto() / 50 - anchoItem);
		items[4] = new Diamante(entorno.ancho() / 9 + anchoPared + anchoItem,
				entorno.alto() - entorno.alto() / 50 - anchoItem);

		mago = new Mago(entorno.ancho() / 2, entorno.alto() / 11);

		this.entorno.iniciar();
	}

	public void tick() {

		sonidoJuego.loop(Clip.LOOP_CONTINUOUSLY);
		entorno.dibujarImagen(fondo, this.anchoPANTALLA / 2, this.altoPANTALLA / 2, 0, 1);

		mago.dibujar(entorno);
		mago.mover();
		mago.dibujarPuntos(entorno);

		for (Viga v : vigas) {
			v.dibujar(entorno);
		}
		
		for (int i = 0; i < mago.getCantVidas(); i++) {
			vidas[i].dibujar(entorno);
		}

		for (Monster m : monsters) {
			if (!m.estaCongelado()) {
				m.dibujar(entorno);
				m.mover();
			} else {
				m.congelate(entorno);
			}
		}

		for (Diamante i : items) {
			if (i != null) {
				i.dibujar(entorno);
			}
		}

		for (Monster m : monsters) {
			if (!m.estaEnAlgunaViga(vigas)) {
				m.caer();
				m.detenerse();
			} else {
				m.continuar();
			}

		}

		for (Monster m : monsters) {
			if (m.chocoPared(entorno)) {
				m.cambiarDireccion();
			} else if (m.teFuistePorHueco(entorno) && !m.estaCongelado()) {
				m.aparecerArriba();
			}
		}

		if (!mago.estaEnAlgunaViga(vigas) && mago.sigueVivo()) {
			mago.detenerse();
			mago.caer();
			mago.recargarHechizo();
		} else {
			mago.continuar();
			for (int h = 0; h < hechizos.length; h++) {
				if (mago.tieneHechizoDisponible() && hechizos[h] == null && mago.sigueVivo()) {
					hechizos[h] = mago.disparar();
					mago.deshabilitarHechizo();
				}
			}
		}

		for (int i = 0; i < hechizos.length; i++) {
			if (hechizos[i] != null) {
				hechizos[i].dibujar(entorno);
				hechizos[i].mover();
				if (hechizos[i].chocasteBorde(entorno, anchoPared)) {
					hechizos[i] = null;
				}
			}
		}

		mago.cargarPoderEspecial();

		if (mago.chocoPared(entorno)) {
			mago.CambiarDireccion();
		}

		if (entorno.sePresiono('a') && mago.isIzquierda() && mago.sigueVivo()) {
			mago.CambiarDireccion();

		}
		if (entorno.sePresiono('d') && !mago.isIzquierda() && mago.sigueVivo()) {
			mago.CambiarDireccion();
		}

		if (mago.teFuistePorHueco(entorno) && mago.sigueVivo()) {
			mago.aparecerArriba();
		}

		if (mago.estaCargadoPoderEspecial()) {
			entorno.cambiarFont("Showcard Gothic", 25, Color.ORANGE);
			entorno.escribirTexto("P.ESPECIAL 'G'", 60, 20);
		}

		if (entorno.sePresiono('g') && mago.estaCargadoPoderEspecial()) {
			mago.dispararPoderEspecial(hechizos);
			mago.reiniciarPoderEspecial();
		}

		for (Monster m : monsters) {
			if (m != null) {
				for (int i = 0; i < hechizos.length; i++) {
					if (hechizos[i] != null && (m.alcanzadoPorHechizo(hechizos[i]) && !m.estaCongelado())) {
						m.congelate(entorno);
						hechizos[i] = null;
						mago.sumarPuntos();
					}
				}
			}
		}

		for (int i = 0; i < items.length; i++) {
			if (items[i] != null && mago.agarroItem(items[i])) {
				items[i] = null;
				mago.sumarPuntosCombo();
				Herramientas.play("sonidoDiamante.wav");
			}
		}

		for (Monster m : monsters) {
			if (m.estaCongelado() && m.loChocoMago(mago)) {
				m.fueChocadoYcongelado();
			}
		}

		for (Monster m : monsters) {
			if (m.estaEnAlgunaViga(vigas) && m.fueCongeladoYchocado()) {
				m.mover();
			} else {
				if (m.fueCongeladoYchocado()) {
					m.detenerse();
					m.caer();
				}
			}

		}

		for (Monster m : monsters) {
			if (m.loChocoMago(mago) && !m.estaCongelado() && mago.getCantVidas() > 0) {
				mago.habilitarRestarVida();
				if (mago.sePuedeRestarVida()) {
					mago.deshabilitarRestarVida();
					mago.aparecerArriba();
					mago.restarUnaVida();
				}
			} else if (m.loChocoMago(mago) && !m.estaCongelado() && mago.getCantVidas() == 0) {
				mago.morir();

			}
		}

		for (Monster m : monsters) {
			if (m.estaCongelado() && m.teFuistePorHueco(entorno)) {
			}
		}

		for (Monster m : monsters) {
			if (m.loChocoMounstruoCongelado(monsters)) {
				if (!m.estaCongelado()) {
					mago.sumarPuntosCombo();
				}
				m.congelate(entorno);
				m.chocadoPorMonstruo();
			}
		}

		for (Monster m : monsters) {
			if (m.estaEnAlgunaViga(vigas) && m.monstruoFueChocadoPorMonstruo()) {
				m.rodarPorElSuelo();
				m.mover();
			} else {
				if (m.monstruoFueChocadoPorMonstruo()) {
					m.detenerse();
					m.caer();
				}
			}
		}

		if (!mago.sigueVivo() && mago.teFuistePorHueco(entorno)) {
			this.mago = null;
		}

		if (!mago.sigueVivo()) {
			entorno.dibujarImagen(FinJuego, entorno.ancho() / 2, entorno.alto() / 2, 0);
			mago.dibujarPuntosFin(entorno);
		}

		for (Monster m : monsters) {
			if (m.MonsterMuerto(entorno)) {
				MonstersMuertos++;
			} else {
				this.MonstersMuertos = 0;
			}
		}

		if (MonstersMuertos > monsters.length) {
			mago.detenerse();
			entorno.dibujarImagen(Ganaste, entorno.ancho() / 2, entorno.alto() / 2, 0);
			mago.dibujarPuntosFin(entorno);
		}

	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
}
