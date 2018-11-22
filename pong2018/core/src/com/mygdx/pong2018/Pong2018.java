package com.mygdx.pong2018;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pantallas.PantallaJuego;
import pantallas.PantallaMenu;
import pantallas.PantallaPreCarga;

public class Pong2018 extends Game {
	
	public boolean conectados = false;
	public boolean empieza    = false;
	
	public SpriteBatch batch;
	
	public PantallaPreCarga pantallaPreCarga;
	public PantallaJuego    pantallaJuego;
	public PantallaMenu     pantallaMenu;

	@Override
	public void create () {
		batch            = new SpriteBatch();
		pantallaPreCarga = new PantallaPreCarga(this);
		pantallaMenu     = new PantallaMenu(this);
		pantallaJuego    = new PantallaJuego(this);
				
		this.setScreen(pantallaMenu);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		pantallaPreCarga.dispose();
		pantallaMenu.dispose();
		//pantallaJuego.dispose();
	}
}
