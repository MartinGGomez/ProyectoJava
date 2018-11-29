package com.red;

import com.game.MainGame;

public class Servidor {

	public HiloServidor hiloServidor;
	
	public Servidor(MainGame app) {
		hiloServidor = new HiloServidor(app);
		hiloServidor.start();
	}
	
}
